package com.croin.croin

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.croin.croin.tensorflow.Classifier
import com.croin.croin.tensorflow.TensorFlowImageClassifier
import kotlinx.android.synthetic.main.activity_identify.*
import kotlinx.android.synthetic.main.content_identify.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IdentifyActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "IdentifyActivity"
        private const val INPUT_WIDTH = 224
        private const val INPUT_HEIGHT = 224
        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128f
        private const val INPUT_NAME = "input"
        private const val OUTPUT_NAME = "final_result"
        private const val MODEL_FILE = "file:///android_asset/optimized_coins_graph.pb"
        private const val LABEL_FILE = "file:///android_asset/coins_labels.txt"
    }

    private var classifier: Classifier? = null
    private var initializeJob: Job? = null
    private var identifiedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
        setSupportActionBar(toolbar)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.title_identifier)
        //toolbar.setIcon(R.drawable.ic_arrow_left_solid)
        toolbar.setDisplayHomeAsUpEnabled(true)

        identifiedBitmap = getExtra("capture")

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
        identifiedBitmap = Bitmap.createScaledBitmap(identifiedBitmap, INPUT_WIDTH, INPUT_HEIGHT, false)

        iCapture.setImageBitmap(identifiedBitmap)

        runOnUiThread {
            classifier?.let {
                try {
                    showRecognizedResult(classifier!!.recognizeImage(identifiedBitmap))
                } catch (e: java.lang.RuntimeException) {
                    Log.e(TAG, "Crashing due to classification.closed() before the recognizer finishes!")
                }
            }
        }
    }

    private fun showRecognizedResult(results: MutableList<Classifier.Recognition>) {
        runOnUiThread {
            if (results.isEmpty()) {
                textResult.text = "Not found"
            } else {
                val coin = results[0].title
                val confidence = results[0].confidence
                textResult.text = when {
                    confidence > 0.95 -> "Wooooho: " + coin
                    confidence > 0.85 -> "Perhaps: " + coin
                    else -> "Maybe: " + coin
                }
                Log.e(TAG, "S'ha arribat fins aqui!!!!!!!")
            }
        }
    }

    private fun initializeTensorClassifier() {
        initializeJob = GlobalScope.launch {
            try {
                classifier = TensorFlowImageClassifier.create(
                        assets, MODEL_FILE, LABEL_FILE, INPUT_WIDTH, INPUT_HEIGHT,
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

    override fun onDestroy() {
        super.onDestroy()
        clearTensorClassifier()
    }

}
