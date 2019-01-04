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
import android.graphics.drawable.BitmapDrawable
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

/**
 * @author Maricel Bros Maimó
 *
 * IdentifyActivity class.
 *
 */
class IdentifyActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var recogintionViewModel: RecognitionViewModel

    private var identifiedBitmap: Bitmap? = null
    private var coinDetected = 0
    private var currencyExchange = 0f
    private var favCurrency: Currency? = null
    private var currentLocation: String? = null


    /**
     * Overrides onCreate default function from activity behaviour.
     *
     * @param savedInstanceState: Bundle
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
        setSupportActionBar(toolbar_idfentify)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.title_identifier)
        //toolbar.setIcon(R.drawable.ic_arrow_left_solid)
        toolbar.setDisplayHomeAsUpEnabled(true)

        identifiedBitmap = getExtra("capture")

        coinDetected = 100 // agafar-ho de DetectorCapture.
        tvDetection.text = "${getString(R.string.value_identified)}: ${coinDetected.toString()} €"
        getFavCurrency()

        ibLocation.setOnClickListener(this)

        //CurrencyViewModel
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        //RecognitionViewModel
        recogintionViewModel = ViewModelProviders.of(this).get(RecognitionViewModel::class.java)

    }


    /**
     * Overrides onSupportNavigateUp from activity behaviour.
     *
     * @return Boolean
     *
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    /**
     * Gets extra information passed from other activity or fragment.
     *
     * @param String key name of the searched object.
     *
     * @return T? object
     *
     */
    inline fun <reified T> Activity.getExtra(extra: String): T? {
        return intent.extras?.get(extra) as? T?
    }


    /**
     * Gets favorite currency from database.
     */
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


    /**
     * Call Currency Exchange (currencyconverterapi.com) API
     * to get the value exchanged if user has favorite currency.
     */
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


    /**
     * Get user current location if user gives her/his permission.
     */
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


    /**
     * Save image captured to internal storage.
     *
     * @return String absolute path of image saved.
     */
    private fun saveImageToInternalStorage(): String {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // The bellow line return a directory in internal storage
        var file = wrapper.getDir( "images", Context.MODE_PRIVATE)

        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            (ivCapture.drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            Log.e("ERROR", "Error saving image!")
        }

        // Return the saved image uri
        return file.absolutePath
    }


    /**
     * Add estra information to activity before startint it.
     *
     * @param String key name
     * @param Any? any value passed like Long, String, Boolean, Float, Double, Int, Parceable, Bitmap, ...
     */
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


    /**
     * Override onCreateOptionsMenu and sets menu in action bar.
     *
     * @param Menu?
     *
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.identify_top_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    /**
     * Override onOptionsItemSelected and actions on click menu items.
     *
     * @param MenuItem selected, in that case only has actin save recognition.
     *
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            //Save recognition
            var recognition = Recognition(null,
                    etName.text.toString(),
                    etDescription.text.toString(),
                    saveImageToInternalStorage(),
                    coinDetected.toDouble(),
                    currentLocation,
                    Calendar.getInstance().time,
                    Calendar.getInstance().time
            )
            recogintionViewModel.insert(recognition)


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


    /**
     * Override onRequestPermissionsResult requests permission to get user location.
     *
     * @param Int request code.
     * @param permissions array of permissions.
     * @param grantResults array.
     *
     */
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


    /**
     * override function onClick from recycler view.
     * Gets location.
     */
    override fun onClick(v: View?) {
        when (v) {
            ibLocation -> {
                getLocation()
            }
        }
    }

}
