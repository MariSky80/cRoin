package com.croin.croin

import android.support.test.espresso.action.ViewActions.click
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.CursorMatchers
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerViewAccessibilityDelegate
import com.croin.croin.network.CurrencyData
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
class AddCurrencyTest{
    @Rule
    @JvmField
    public val rule = ActivityTestRule(MainActivity::class.java)

    private var CURRENCY_SELECTED = "UNITED STATES DOLLAR"

    /**
     * Enter currency fragment, select first currency and add.
     */
    @Test
    fun enterFragmentCurrency() {

        //Select currency section
        onView((withId(R.id.navigation_currency)))
                .perform(click())

        //Click to spinner to select currency
        onView(withId(R.id.spCurrencies))
                .perform(click())


        //Click add currency
        onView(withId(R.id.ibAddCurrency))
                .perform(click())

        //Click yes to dialog
        onView(withId(android.R.id.button1))
                .perform(click())



    }

    /**
     * Marc added currency to favorite.
     */
    @Test
    fun selectFavorite() {
//        onView(withId(R.id.rvRecognition)).perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, onView((withId(R.id.ibFav)))
//                        .perform(click()) as ViewAction))
//
//        )
    }


}