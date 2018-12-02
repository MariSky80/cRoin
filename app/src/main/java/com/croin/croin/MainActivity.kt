package com.croin.croin

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import BottomNavigationViewHelper
import android.support.v4.app.Fragment


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_historical -> {
                replaceFragment(HistoryFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calculations -> {
                replaceFragment(CalculatorFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_currency -> {
                replaceFragment(CurrencySettingsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_language -> {
                replaceFragment(LanguageFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                replaceFragment(HomeFragment())
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

        replaceFragment(HomeFragment())


    }



    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
