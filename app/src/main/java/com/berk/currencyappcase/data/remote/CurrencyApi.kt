package com.berk.currencyappcase.data.remote

import com.berk.currencyappcase.data.remote.models.CurrencyResponse
import com.berk.currencyappcase.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("v1/latest")
    suspend fun getRates(
        @Query("apikey") apikey: String = API_KEY,
        @Query("currencies") currencies: String,
        @Query("base_currency") baseCurrency: String = "TRY"
    ): Response<CurrencyResponse>
}