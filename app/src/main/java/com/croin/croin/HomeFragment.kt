package com.croin.croin

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * @author Maricel Bros Maimó
 *
 * HomeFragment subclass.
 *
 */
class HomeFragment : Fragment(), View.OnClickListener {

    private val CAMERA_REQUEST_CODE = 12345
    private val REQUEST_GALLERY_CAMERA = 54654


    /**
     * Overrides onCreateView default function from fragment behaviour.
     *
     * @param LayoutInflater
     * @param ViewGroup?
     * @param Bundle?
     *
     * @return View?
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewHome: View = inflater!!.inflate(R.layout.fragment_home, container, false)
        val ibIdentifier: ImageButton = viewHome.findViewById(R.id.ibIdentifier)

        ibIdentifier.layoutParams.height = 500
        ibIdentifier.layoutParams.width = 500


        animateIdentifyButton(ibIdentifier)

        ibIdentifier.setOnClickListener(this)

        return viewHome


    }


    /**
     * Gives animations to a buttons passed.
     *
     * @param ImageButton to animate.
     *
     */
    private fun animateIdentifyButton(ib: ImageButton?) {
        // arcTo() and PathInterpolator only available on API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val identifyAnimatorSet = AnimatorInflater.loadAnimator(activity, R.animator.identify_button_effects) as AnimatorSet
            identifyAnimatorSet.setTarget(ib)
            identifyAnimatorSet.start()

        } else {
            //No animation
        }
    }


    /**
     * Overrides onClick from ImageButton listener.
     * Call the camera function but before asks permissions if needed.
     *
     * @param View
     *
     */
    override fun onClick(v: View?) {
        when (v) {
            ibIdentifier -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_GALLERY_CAMERA)
                    } else {
                        openCamera()
                    }
                } else {
                    openCamera()
                }
            }
        }
    }


    /**
     * Open an intent to open the camera after the user accepts permissions.
     *
     */
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }


    /**
     * Overrides onRequestPermissionsResult and asks permissions if needed.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     *
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(activity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * Overrides onActivityResult and if the user took a photo it is passed to IdentifyActivity.
     *
     * @param requestCode
     * @param resultCode
     * @param Intent
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {

                    val extras = data?.getExtras()
                    val imageBitmap = extras?.get("data") as Bitmap

                    val intentIdentifier = Intent(activity, IdentifyActivity::class.java)
                    intentIdentifier.addExtra("capture", imageBitmap)
                    startActivity(intentIdentifier)
                }
            }
        }
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


}
