package com.berk.currencyappcase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.berk.currencyappcase.data.local.model.PastTransactions

@Dao
interface PastTransactionsDao {
    @Insert
    suspend fun insertTransaction(transaction: PastTransactions)

    @Query("SELECT * FROM past_transactions ORDER BY id")
    suspend fun getAllPastTransactions(): List<PastTransactions>

}
