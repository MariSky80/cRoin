package com.croin.croin

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_historical -> {
                message.setText(R.string.title_historical)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calculations -> {
                message.setText(R.string.title_calculations)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_currency -> {
                message.setText(R.string.title_currency)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_language -> {
                message.setText(R.string.title_language)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
