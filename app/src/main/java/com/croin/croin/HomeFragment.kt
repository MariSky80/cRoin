package com.croin.croin

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * @author Maricel Bros Maimó
 *
 * HomeFragment subclass.
 *
 */
class HomeFragment : Fragment(), View.OnClickListener {

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
                val intent = Intent(context, DetectorActivity::class.java)
                startActivity(intent)
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
