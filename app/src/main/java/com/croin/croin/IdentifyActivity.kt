package com.croin.croin

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.croin.croin.database.entity.Currency
import com.croin.croin.models.CurrencyViewModel
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

class IdentifyActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "IdentifyActivity"
        private const val INPUT_SIZE_TF = 224
        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128f
        private const val INPUT_NAME = "input"
        private const val OUTPUT_NAME = "final_result"
        private const val MODEL_FILE = "file:///android_asset/optimized_coins_graph.pb"
        private const val LABEL_FILE = "file:///android_asset/coins_labels.txt"
    }

    private lateinit var currencyViewModel: CurrencyViewModel

    private var classifier: Classifier? = null
    private var initializeJob: Job? = null
    private var identifiedBitmap: Bitmap? = null
    private var coinDetected = 0
    private var currencyExchange = 0f
    private var favCurrency: Currency? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
        setSupportActionBar(toolbar_idfentify)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.title_identifier)
        //toolbar.setIcon(R.drawable.ic_arrow_left_solid)
        toolbar.setDisplayHomeAsUpEnabled(true)

        identifiedBitmap = getExtra("capture")


        //CurrencyViewModel
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

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

            }
        })


    }

    private fun getCurrencyExchange() {
        /*DEBUG RESPONSE*/
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
                    tvCurrencyExchange.setText("${getString(R.string.currency_exchange)}: ${exchange} ${favCurrency!!.symbol} (${favCurrency!!.id})")
                } else {
                    tvCurrencyExchange.setText(getString(R.string.no_currency_exchange))
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTensorClassifier()
    }

}
