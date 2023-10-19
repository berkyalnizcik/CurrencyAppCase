package com.berk.currencyappcase.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berk.currencyappcase.data.local.AppDatabase
import com.berk.currencyappcase.data.local.model.PastTransactions
import com.berk.currencyappcase.data.remote.models.Data
import com.berk.currencyappcase.main.MainRepository
import com.berk.currencyappcase.util.Constants.API_KEY
import com.berk.currencyappcase.util.DispatcherProvider
import com.berk.currencyappcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Math.round
import javax.inject.Inject

@HiltViewModel
class ExchangeDialogViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _convertOperation = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val convertOperation: StateFlow<CurrencyEvent> = _convertOperation

    fun insertTransactionToDatabase(transaction: PastTransactions, context: Context) {
        val database = AppDatabase.getInstance(context)
        val dao = database.pastTransactionsDao()
        viewModelScope.launch {
            dao.insertTransaction(transaction)
        }
    }

    fun exchangeCurrency(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String,
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _convertOperation.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _convertOperation.value = CurrencyEvent.Loading
            when (val ratesResponse = repository.getRates(API_KEY, toCurrency, fromCurrency)) {
                is Resource.Error -> _convertOperation.value =
                    CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.data
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _convertOperation.value = CurrencyEvent.Failure("Unexpected Error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _convertOperation.value = CurrencyEvent.Success(
                            "$convertedCurrency"
                        )
                    }
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
