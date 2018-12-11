package com.croin.croin

import android.content.res.Resources
import android.net.Uri
import android.provider.MediaStore
import android.support.test.espresso.action.ViewActions.click
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.croin.croin.network.CurrencyData
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.UiController
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.core.internal.deps.guava.io.ByteStreams
import android.support.test.runner.intent.IntentCallback
import android.support.test.runner.intent.IntentMonitorRegistry
import android.support.v7.widget.RecyclerView
import android.view.View
import com.croin.croin.adapters.CurrencyAdapter
import java.util.regex.Matcher


@RunWith(AndroidJUnit4::class)
class AddCurrencyTest{
    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    var CURRENCY_SELECTED: CurrencyData = CurrencyData("Barbadian Dollar", "BBD", "$" )
    var CURRENCY_FAVORITE: CurrencyData = CurrencyData("Japanese Yen", "JPY", "¥" )

    /**
     * Enter to the currency fragment and select Barbarian Dollar currency and add to list
     * Then mark as favourite
     */
    @Test
    fun addCurrencyToList() {

        //Select currency section
        onView((withId(R.id.navigation_currency)))
                .perform(click())

        //Click to spinner to select currency
        onView(withId(R.id.spCurrencies))
                .perform(click())

        //Select currency (for example American US)
        onData(allOf(`is`(instanceOf(CurrencyData::class.java)),
                `is`(CURRENCY_SELECTED)))
                .perform(click())

        //Click add currency
        onView(withId(R.id.ibAddCurrency))
                .perform(click())

        //Click yes to dialog
        onView(withId(android.R.id.button1))
                .perform(click())

        //Click to spinner to select currency
        onView(withId(R.id.spCurrencies))
                .perform(click())

        //Select currency (for example American US)
        onData(allOf(`is`(instanceOf(CurrencyData::class.java)),
                `is`(CURRENCY_FAVORITE)))
                .perform(click())

        //Click add currency
        onView(withId(R.id.ibAddCurrency))
                .perform(click())

        //Click yes to dialog
        onView(withId(android.R.id.button1))
                .perform(click())

    }
}