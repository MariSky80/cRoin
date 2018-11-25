package com.croin.croin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        setSupportActionBar(toolbar)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.title_language)
        //toolbar.setIcon(R.drawable.ic_arrow_left_solid)
        toolbar.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
