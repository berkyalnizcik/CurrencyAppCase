package com.berk.currencyappcase.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berk.currencyappcase.data.remote.models.Data
import com.berk.currencyappcase.util.Constants
import com.berk.currencyappcase.util.DispatcherProvider
import com.berk.currencyappcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Math.round
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: Map<String, Double>) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _convertOperation = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val convertOperation: StateFlow<CurrencyEvent> = _convertOperation

    fun convert(
        amount: String,
        fromCurrency: String,
        toCurrency: List<String>
    ) {
        val fromAmount = amount.toFloatOrNull()
        if (fromAmount == null) {
            _convertOperation.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _convertOperation.value = CurrencyEvent.Loading
            when (val ratesResponse = repository.getRates(
                Constants.API_KEY,
                toCurrency.joinToString(","),
                fromCurrency
            )) {
                is Resource.Error -> _convertOperation.value =
                    CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.data
                    val rateList = toCurrency.map {
                        it to getRateForCurrency(it, rates)
                    }
                    val convertedCurrency = rateList.map { (key, value) ->
                        key to (round(fromAmount * (value ?: 0.0) * 100) / 100).toDouble()
                    }
                    _convertOperation.value = CurrencyEvent.Success(
                        convertedCurrency.toMap()
                    )
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Data) = when (currency) {
        "EUR" -> rates.EUR
        "RUB" -> rates.RUB
        "CNY" -> rates.CNY
        "USD" -> rates.USD
        "GBP" -> rates.GBP
        "TRY" -> rates.TRY
        else -> null
    }
}
