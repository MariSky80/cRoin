package com.croin.croin

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.croin.croin.database.entity.Currency
import com.croin.croin.database.entity.Recognition
import com.croin.croin.models.CurrencyViewModel
import com.croin.croin.models.RecognitionViewModel
import com.croin.croin.network.CurrencyService
import com.croin.croin.tensorflow.Classifier
import com.croin.croin.tensorflow.TensorFlowImageClassifier
import com.croin.croin.utilities.URL_CURRENCY_CONVERTER
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_identify.*
import kotlinx.android.synthetic.main.content_identify.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class IdentifyActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "IdentifyActivity"
        private const val INPUT_SIZE_TF = 224
        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128f
        private const val INPUT_NAME = "input"
        private const val OUTPUT_NAME = "final_result"
        private const val MODEL_FILE = "file:///android_asset/optimized_coins_graph.pb"
        private const val LABEL_FILE = "file:///android_asset/coins_labels.txt"
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var recogintionViewModel: RecognitionViewModel

    private var classifier: Classifier? = null
    private var initializeJob: Job? = null
    private var identifiedBitmap: Bitmap? = null
    private var coinDetected = 0
    private var currencyExchange = 0f
    private var favCurrency: Currency? = null
    private var currentLocation: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
        setSupportActionBar(toolbar_idfentify)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.title_identifier)
        //toolbar.setIcon(R.drawable.ic_arrow_left_solid)
        toolbar.setDisplayHomeAsUpEnabled(true)

        identifiedBitmap = getExtra("capture")

        ibLocation.setOnClickListener(this)

        //CurrencyViewModel
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        //RecognitionViewModel
        recogintionViewModel = ViewModelProviders.of(this).get(RecognitionViewModel::class.java)

        initializeTensorClassifier()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inline fun <reified T> Activity.getExtra(extra: String): T? {
        return intent.extras?.get(extra) as? T?
    }

    private fun onImageCaptured() {

        ivCapture.setImageBitmap(identifiedBitmap)

        val outWidthTF: Int
        val outHeightTF: Int
        val inWidthTF: Int = identifiedBitmap!!.width
        val inHeightTF: Int = identifiedBitmap!!.height
        if(inWidthTF > inHeightTF) {
            outWidthTF = INPUT_SIZE_TF
            outHeightTF = (inHeightTF * INPUT_SIZE_TF) / inWidthTF
        } else {
            outHeightTF = INPUT_SIZE_TF
            outWidthTF = (inWidthTF * INPUT_SIZE_TF) / inHeightTF
        }

        identifiedBitmap = Bitmap.createScaledBitmap(identifiedBitmap, outWidthTF, outHeightTF, false)



        runOnUiThread {
            classifier?.let {
                try {
                    showRecognizedResult(classifier!!.recognizeImage(identifiedBitmap))
                } catch (e: java.lang.RuntimeException) {
                    Log.e(TAG, "Crashing due to classification.closed() before the recognizer finishes! " + e)
                }
            }
        }
    }

    private fun showRecognizedResult(results: MutableList<Classifier.Recognition>) {
        runOnUiThread {
            if (results.isEmpty()) {
                coinDetected = 0
                tvDetection.text = getString(R.string.not_found)
            } else {
                coinDetected = results[0].title.toInt()
                tvDetection.text = "${getString(R.string.value_identified)}: ${coinDetected.toString()} €"
                getFavCurrency()
            }
        }
    }

    private fun initializeTensorClassifier() {
        initializeJob = GlobalScope.launch {
            try {
                classifier = TensorFlowImageClassifier.create(
                        assets, MODEL_FILE, LABEL_FILE, INPUT_SIZE_TF, INPUT_SIZE_TF,
                        IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME)
                runOnUiThread {
                    onImageCaptured()
                }
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }


    private fun clearTensorClassifier() {
        initializeJob?.cancel()
        classifier?.close()
    }

    private fun getFavCurrency() {
        currencyViewModel.preferred.observe(this@IdentifyActivity, Observer { currency ->
            currency?.let {
                favCurrency = it
                getCurrencyExchange()
            } ?: run {
                tvCurrencyExchange.text = getString(R.string.no_currency_exchange)
            }
        })


    }

    private fun getCurrencyExchange() {
        //DEBUG RESPONSE
        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        val serviceCurrencies = Retrofit.Builder()
                .baseUrl(URL_CURRENCY_CONVERTER)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
                .create(CurrencyService::class.java)

        GlobalScope.launch(Dispatchers.Main) {

            favCurrency?.let {
                val sendCurrencies = "EUR_${favCurrency!!.id}"

                val result = serviceCurrencies.retrieveCurrencyValue(sendCurrencies, "ultra").await()

                currencyExchange = result.values.first()

                val exchange: Float = currencyExchange * coinDetected * 1f
                if (currencyExchange > 0f) {
                    tvCurrencyExchange.text = "${getString(R.string.currency_exchange)}: ${exchange} ${favCurrency!!.symbol} (${favCurrency!!.id})"
                } else {
                    tvCurrencyExchange.text = getString(R.string.no_currency_exchange)
                }
            }

        }
    }

    private fun getLocation() {

        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitude = location!!.latitude
                var longitude = location!!.longitude
                currentLocation = "$latitude,$longitude"
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)

        try {
            Toast.makeText(applicationContext, getString(R.string.location_saved), Toast.LENGTH_SHORT).show()
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex:SecurityException) {
            Toast.makeText(applicationContext, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveImageToInternalStorage(): String {

        // Get the bitmap from drawable object
        val bitmap = identifiedBitmap

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        var fileName = "${UUID.randomUUID()}.jpg"
        file = File(file, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            //e.printStackTrace()
            Log.e("ERROR", "Error saving image!")
        }

        // Return the saved image uri
        return fileName
    }

    private fun Intent.addExtra(key: String, value: Any?) {
        when (value) {
            is Long -> putExtra(key, value)
            is String -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Int -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is Bitmap -> putExtra(key, value)
            //Add other types when needed
        }
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.identify_top_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            //Save image into internal storage
            val imageName = saveImageToInternalStorage()

            //Save recognition
            var recognition = Recognition(null,
                    tvName.text as String,
                    tvDescription.text as String,
                    imageName,
                    coinDetected.toDouble(),
                    currentLocation,
                    Calendar.getInstance().time,
                    Calendar.getInstance().time
            )
            recogintionViewModel.insert(recognition)
            Toast.makeText(this,getString(R.string.identify_saved),Toast.LENGTH_LONG).show()

            val intentMain = Intent(this, MainActivity::class.java)
            intentMain.addExtra("fragment", "History")
            startActivity(intentMain)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> {
                    lateinit var dialog: AlertDialog

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.dialog_location_title))
                    builder.setMessage(getString(R.string.dialog_location_description))

                    val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
                        when(which){
                            DialogInterface.BUTTON_POSITIVE -> {}
                        }
                    }

                    builder.setPositiveButton(R.string.dialog_yes,dialogClickListener)

                    dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTensorClassifier()
    }

    override fun onClick(v: View?) {
        when (v) {
            ibLocation -> {
                getLocation()
            }
        }
    }

}
