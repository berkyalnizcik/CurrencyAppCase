package com.berk.currencyappcase.ui.pastTransactions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berk.currencyappcase.R
import com.berk.currencyappcase.data.local.model.PastTransactions
import com.berk.currencyappcase.databinding.ItemPastTransactionsBinding

class PastTransactionsAdapter(private val transactions: List<PastTransactions>) :
    RecyclerView.Adapter<PastTransactionsAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(binding: ItemPastTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val pastTransactionsName: TextView = binding.tvPastTransactionsName
        val pastTransactionsValue: TextView = binding.tvPastTransactionsValue
        val pastTransactionsDate: TextView = binding.tvPastTransactionsDate
        val pastTransactionsAmount: TextView = binding.tvAmount
        val pastTransactionsSymbol: ImageView = binding.imgPastTransactions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPastTransactionsBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        when (transaction.toCurrencyName) {
            "USD" -> {
                holder.pastTransactionsSymbol.setImageResource(R.drawable.img_united_states)
                holder.pastTransactionsValue.text = "$" + transaction.toCurrencyValue
            }
            "EUR" -> {
                holder.pastTransactionsSymbol.setImageResource(R.drawable.img_europe)
                holder.pastTransactionsValue.text = "€" + transaction.toCurrencyValue
            }
            "GBP" -> {
                holder.pastTransactionsSymbol.setImageResource(R.drawable.img_united_kingdom)
                holder.pastTransactionsValue.text = "£" + transaction.toCurrencyValue
            }
            "RUB" -> {
                holder.pastTransactionsSymbol.setImageResource(R.drawable.img_russia)
                holder.pastTransactionsValue.text = "₽" + transaction.toCurrencyValue
            }
            "CNY" -> {
                holder.pastTransactionsSymbol.setImageResource(R.drawable.img_china)
                holder.pastTransactionsValue.text = "¥" + transaction.toCurrencyValue
            }
            else -> {
                holder.pastTransactionsSymbol.setImageResource(R.drawable.img_tr)
                holder.pastTransactionsValue.text = "₺" + transaction.toCurrencyValue
            }
        }
        holder.pastTransactionsName.text = transaction.toCurrencyName
        holder.pastTransactionsDate.text = transaction.transactionDate
        holder.pastTransactionsAmount.text = transaction.fromCurrencyValue
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}