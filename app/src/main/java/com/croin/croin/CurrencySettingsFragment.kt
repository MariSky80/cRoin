package com.croin.croin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.croin.croin.data.AppDatabase
import com.croin.croin.data.dao.CurrencyDao
import com.croin.croin.network.CurrencyMoshi
import com.croin.croin.utilities.CURRENCY_DATA_FILENAME
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException


/**
 * @author Maricel Bros Maimó
 *
 * CurrencySettingsFragment subclass.
 *
 */
class CurrencySettingsFragment : Fragment() {

    private var db: AppDatabase? = null
    private var currencyDao: CurrencyDao? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewCurrency = inflater!!.inflate(R.layout.fragment_currency, container, false)


        val jsonResponse = context.applicationContext.assets.open(CURRENCY_DATA_FILENAME).bufferedReader().use{
            it.readText()
        }

        val mCurrencyMoshi = Moshi.Builder().build()

        val type = Types.newParameterizedType(List::class.java, CurrencyMoshi::class.java)
        val jsonAdapter: JsonAdapter<List<CurrencyMoshi>> = mCurrencyMoshi.adapter(type)


        try {
            val currencies = jsonAdapter.fromJson(jsonResponse)
            Log.d("Size", "$currencies!!.size size")

            for (i in currencies!!.indices) {
                Log.d("Person", currencies.get(i).toString())
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }


        return viewCurrency
    }


}// Required empty public constructor


/*
* DATABASE READER
*
* Observable.fromCallable({

            db = AppDatabase.getInstance(context = this@CurrencySettingsFragment.context)

            currencyDao = db?.currencyDao()

            db?.currencyDao()?.getCurrencies()


        }).doOnNext({ list ->
            var finalString = ""
            list?.map { finalString+= it.name+" - " }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
* */


/*

Get exchange!

//Loging responses.
        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()



        val serviceCurrencies = Retrofit.Builder()
                .baseUrl(URL_CURRENCY_CONVERTER)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                //.client(client)
                .build()
                .create(CurrencyService::class.java)

        GlobalScope.launch {
            val result = serviceCurrencies.retrieveCurrencyValue("EUR_USD", "ultra").await()


            println(result.values)
        }


 */
