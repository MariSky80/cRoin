package com.croin.croin.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("convert")
    fun retrieveCurrencyValue(@Query("q") currencies: String, @Query("compact") ultra: String): Deferred<Map<String, Float>>
}


