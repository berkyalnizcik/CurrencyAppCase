package com.berk.currencyappcase.main

import com.berk.currencyappcase.data.remote.models.CurrencyResponse
import com.berk.currencyappcase.util.Resource

interface MainRepository {
    suspend fun getRates(currencies: String, baseCurrency: String): Resource<CurrencyResponse>
}