package com.berk.currencyappcase.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berk.currencyappcase.R
import com.berk.currencyappcase.databinding.ItemCurrenciesBinding

class CurrenciesAdapter(private val convertedAmounts: Map<String, Double>) :
    RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder>() {

    private var onItemClickListener: ((currency: String) -> Unit)? = null

    fun setOnItemClickListener(listener: (currency: String) -> Unit) {
        onItemClickListener = listener
    }

    class CurrencyViewHolder(binding: ItemCurrenciesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val currencyName: TextView = binding.tvCurrenciesName
        val currencyImg: ImageView = binding.imgCurrencies
        val currencyValue: TextView = binding.tvCurrenciesValue
        val currencyBtn: TextView = binding.btnBuy

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrenciesBinding.inflate(inflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = convertedAmounts.keys.toList()[position]
        val amount = convertedAmounts[currency]
        when (currency) {
            "USD" -> {
                holder.currencyImg.setImageResource(R.drawable.img_united_states)
            }
            "EUR" -> {
                holder.currencyImg.setImageResource(R.drawable.img_europe)
            }
            "GBP" -> {
                holder.currencyImg.setImageResource(R.drawable.img_united_kingdom)
            }
            "RUB" -> {
                holder.currencyImg.setImageResource(R.drawable.img_russia)
            }
            "CNY" -> {
                holder.currencyImg.setImageResource(R.drawable.img_china)
            }
            else -> {
                holder.currencyImg.setImageResource(R.drawable.img_tr)
            }
        }
        holder.currencyName.text = currency
        holder.currencyValue.text = amount.toString()
        holder.currencyBtn.setOnClickListener {
            onItemClickListener?.invoke(currency)
        }
    }

    override fun getItemCount(): Int {
        return convertedAmounts.size
    }
}