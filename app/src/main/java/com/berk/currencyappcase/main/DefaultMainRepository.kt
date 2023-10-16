package com.berk.currencyappcase.main

import com.berk.currencyappcase.data.remote.CurrencyApi
import com.berk.currencyappcase.data.remote.models.CurrencyResponse
import com.berk.currencyappcase.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {

    override suspend fun getRates(
        currencies: String,
        baseCurrency: String
    ): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(currencies, baseCurrency)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }
}
