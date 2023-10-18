package com.berk.currencyappcase.ui.pastTransactions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.berk.currencyappcase.data.local.AppDatabase
import com.berk.currencyappcase.databinding.FragmentPastTransactionsBinding
import com.berk.currencyappcase.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PastTransactionsFragment :
    BaseFragment<FragmentPastTransactionsBinding>(FragmentPastTransactionsBinding::inflate) {

    private lateinit var pastTransactionsAdapter: PastTransactionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRoomDb()
    }

    private fun initRoomDb() {
        val database = AppDatabase.getInstance(requireContext())
        val dao = database.pastTransactionsDao()
        lifecycleScope.launch {
            val pastTransactionsList = dao.getAllPastTransactions()
            if (pastTransactionsList.isEmpty()) {
                Toast.makeText(requireContext(), "Henüz hiç işlem yok.", Toast.LENGTH_SHORT).show()
            } else {
                pastTransactionsAdapter = PastTransactionsAdapter(pastTransactionsList)
                binding.rcvPastTransactions.layoutManager = LinearLayoutManager(requireContext())
                binding.rcvPastTransactions.adapter = pastTransactionsAdapter
            }
        }
    }
}