package com.croin.croin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.croin.croin.data.AppDatabase
import com.croin.croin.data.dao.CurrencyDao
import com.croin.croin.network.CurrencyMoshi
import kotlinx.android.synthetic.main.fragment_currency.*
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
class CurrencySettingsFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private var db: AppDatabase? = null
    private var currencyDao: CurrencyDao? = null
    private var spContent: ArrayList<CurrencyMoshi> = arrayListOf()
    private var selectedCurrency: CurrencyMoshi? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewCurrency: View = inflater!!.inflate(R.layout.fragment_currency, container, false)

        // Image button Add Currency
        val ibAddCurrency: ImageButton = viewCurrency.findViewById(R.id.ibAddCurrency)

        ibAddCurrency.minimumHeight = 32
        ibAddCurrency.minimumWidth = 28

        ibAddCurrency.setOnClickListener(this)


        val spCurrencies: Spinner = viewCurrency.findViewById(R.id.spCurrencies)
        spContent = getCurrenciesFromJSon()

        if (spContent != null) {
            val aCurrencies = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, spContent)
            aCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCurrencies.adapter = aCurrencies
        }

        spCurrencies!!.setOnItemSelectedListener(this)

        return viewCurrency
    }


    private fun getCurrenciesFromJSon(): ArrayList<CurrencyMoshi> {

        val al: ArrayList<CurrencyMoshi> = arrayListOf()

        val jsonResponse = context.applicationContext.assets.open(CURRENCY_DATA_FILENAME).bufferedReader().use{
            it.readText()
        }

        val mCurrencyMoshi = Moshi.Builder().build()

        val type = Types.newParameterizedType(List::class.java, CurrencyMoshi::class.java)
        val jsonAdapter: JsonAdapter<List<CurrencyMoshi>> = mCurrencyMoshi.adapter(type)


        try {
            val currencies = jsonAdapter.fromJson(jsonResponse)
            for (i in currencies!!.indices) {
                al.add(currencies[i])
            }

        } catch (e: IOException) {
            //e.printStackTrace()
        }
        return al
    }


    override fun onClick(v: View?) {
        when (v) {
            ibAddCurrency -> {
                println("ey!")
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedCurrency = parent.selectedItem as CurrencyMoshi

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Nothing to do here.
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
