package com.croin.croin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.croin.croin.data.AppDatabase
import com.croin.croin.data.dao.CurrencyAddedDao
import com.croin.croin.data.dao.CurrencyDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



/**
 * @author Maricel Bros Maimó
 *
 * CurrencySettingsFragment subclass.
 *
 */
class CurrencySettingsFragment : Fragment() {

    private val URLCURRENCYCONVERTER = "https://free.currencyconverterapi.com/api/v6/"

    private var db: AppDatabase? = null
    private var currencyDao: CurrencyDao? = null
    private var currencyAddedDao: CurrencyAddedDao? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewCurrency = inflater!!.inflate(R.layout.fragment_currency, container, false)


        Observable.fromCallable({
            db = AppDatabase.getAppDataBase(context = this@CurrencySettingsFragment.context)
            currencyDao = db?.currencyDao()

            db?.currencyDao()?.getCurrencies()


        }).doOnNext({ list ->
            var finalString = ""
            list?.map { finalString+= it.name+" - " }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()


        return viewCurrency
    }

}// Required empty public constructor


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
                .baseUrl("https://free.currencyconverterapi.com/api/v6/")
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
