package com.berk.currencyappcase.ui.pastTransactions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berk.currencyappcase.data.local.model.PastTransactions
import com.berk.currencyappcase.databinding.ItemPastTransactionsBinding

class PastTransactionsAdapter(private val transactions: List<PastTransactions>) :
    RecyclerView.Adapter<PastTransactionsAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(binding: ItemPastTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val pastTransactionsName: TextView = binding.tvPastTransactionsName
        val pastTransactionsValue: TextView = binding.tvPastTransactionsValue
        val pastTransactionsDate: TextView = binding.tvPastTransactionsDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPastTransactionsBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.pastTransactionsName.text = transaction.currencyName
        holder.pastTransactionsValue.text = transaction.currencyValue
        holder.pastTransactionsDate.text = transaction.currencyDate
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}