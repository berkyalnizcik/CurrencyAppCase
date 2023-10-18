package com.berk.currencyappcase.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berk.currencyappcase.data.local.dao.PastTransactionsDao
import com.berk.currencyappcase.data.local.model.PastTransactions

@Database(entities = [PastTransactions::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pastTransactionsDao(): PastTransactionsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "past_transactions_db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}