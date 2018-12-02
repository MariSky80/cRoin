package com.croin.croin

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


import kotlinx.android.synthetic.main.activity_identify.*
import kotlinx.android.synthetic.main.content_identify.*

class IdentifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
        setSupportActionBar(toolbar)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.title_identifier)
        //toolbar.setIcon(R.drawable.ic_arrow_left_solid)
        toolbar.setDisplayHomeAsUpEnabled(true)

        iCapture.setImageBitmap(getExtra("capture"))

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inline fun <reified T> Activity.getExtra(extra: String): T? {
        return intent.extras?.get(extra) as? T?
    }




}
