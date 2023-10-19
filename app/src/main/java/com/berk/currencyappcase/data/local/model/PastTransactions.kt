package com.berk.currencyappcase.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "past_transactions")
data class PastTransactions(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val toCurrencyName: String = "",
    val toCurrencyValue: String = "",
    val fromCurrencyValue: String = "",
    val transactionDate: String = ""
)
