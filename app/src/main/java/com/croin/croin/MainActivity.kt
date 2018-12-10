package com.croin.croin

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.croin.croin.helpers.BottomNavigationViewHelper
import android.support.v4.app.Fragment
import android.view.View
import java.util.*

/**
 * @author Maricel Bros Maimó
 *
 * MainActivity class.
 *
 */
class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        toolbarManagement(item.itemId)

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
            R.id.navigation_home -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false


    }


    /**
     * Overrides onCreate default function from activity behaviour.
     *
     * @param savedInstanceState: Bundle
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbarManagement(R.id.navigation_home)
        setContentView(R.layout.activity_main)

        BottomNavigationViewHelper.disableShiftMode(navigation)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var receivedFragment: String? = getExtra("fragment")

        when(receivedFragment) {
            null -> replaceFragment(HomeFragment())
            "History" -> replaceFragment(HistoryFragment())
        }


    }

    inline fun <reified T> Activity.getExtra(extra: String): T? {
        return intent.extras?.get(extra) as? T?
    }

    /**
     * Overrides onBackPressed default function from default behaviour.
     *
     */
    override fun onBackPressed() {
        val selectedItemId = navigation.selectedItemId

        if(R.id.navigation_home != selectedItemId) {

            //Select home item.
            val menuItem = navigation!!.menu.getItem(0)
            menuItem.isChecked = menuItem.itemId == R.id.navigation_home
            toolbarManagement(R.id.navigation_home)
            navigation!!.menu.findItem(selectedItemId)
            replaceFragment(HomeFragment())

        } else {
            super.onBackPressed()
        }
    }

    /**
     * Replace fragment depending of selected item on bottom navigation menu.
     *
     * @param fragment: Fragment
     *
     */
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
    }


    /**
     * Change toolbar characteristics depending of selected item on bottoom navigation menu.
     *
     * @param itemId: Int
     *
     */
    private fun toolbarManagement(itemId: Int) {
        when (itemId) {
            R.id.navigation_historical -> {
                toolbar_main!!.title = getString(R.string.title_historical)
                toolbar_main.visibility = View.VISIBLE
            }
            R.id.navigation_calculations -> {
                toolbar_main!!.title = getString(R.string.title_calculations)
                toolbar_main?.visibility = View.VISIBLE
            }
            R.id.navigation_currency -> {
                toolbar_main!!.title = getString(R.string.title_currency)
                toolbar_main?.visibility = View.VISIBLE
            }
            R.id.navigation_home -> {
                toolbar_main?.visibility = View.GONE
            }
        }
    }

}
