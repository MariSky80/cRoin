package com.croin.croin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * @author Maricel Bros Maimó
 *
 * HomeFragment subclass.
 *
 */
class CurrencySettingsFragment : Fragment() {

    private val URLCURRENCYCONVERTER = "https://free.currencyconverterapi.com/api/v6/"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewCurrency = inflater!!.inflate(R.layout.fragment_currency, container, false)





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
