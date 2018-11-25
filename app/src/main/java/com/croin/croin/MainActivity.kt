package com.croin.croin

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import BottomNavigationViewHelper
import android.content.Intent


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_historical -> {
                val intent = Intent(
                        this@MainActivity,
                        HistoryActivity::class.java
                )
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calculations -> {
                val intent = Intent(
                        this@MainActivity,
                        CalculatorActivity::class.java
                )
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_currency -> {
                val intent = Intent(
                        this@MainActivity,
                        CurrencyActivity::class.java
                )
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_language -> {
                val intent = Intent(
                        this@MainActivity,
                        LanguageActivity::class.java
                )
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = navigation as BottomNavigationView
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
