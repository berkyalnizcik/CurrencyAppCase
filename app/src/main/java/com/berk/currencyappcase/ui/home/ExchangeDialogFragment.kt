package com.berk.currencyappcase.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.berk.currencyappcase.R
import com.berk.currencyappcase.data.local.model.PastTransactions
import com.berk.currencyappcase.databinding.FragmentExchangeDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExchangeDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentExchangeDialogBinding? = null
    private val binding get() = _binding!!
    private val args: ExchangeDialogFragmentArgs by navArgs()
    private val viewModel: ExchangeDialogViewModel by viewModels()
    private var toCurrencyAmount = ""
    private var purchasedAmount = ""
    private var purchasedSymbol = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
        initAmount()
        onClickBuy()
    }

    private fun onClickBuy() {
        binding.btnBuy.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currencyDate = dateFormat.format(calendar.time)
            if (args.fromCurrencyAmount.toInt() >= purchasedAmount.toInt()) {
                val transaction = PastTransactions(
                    toCurrencyName = args.toCurrency,
                    toCurrencyValue = toCurrencyAmount,
                    fromCurrencyValue = purchasedSymbol + purchasedAmount,
                    transactionDate = currencyDate
                )
                viewModel.insertTransactionToDatabase(transaction, requireContext())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.success_text),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.not_enough_money_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initAmount() {
        val amountEditText = binding.edtBuyAmount

        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val amountText = s.toString()
                if (amountText.isNotBlank()) {
                    toCurrencyAmount = binding.edtBuyAmount.text.toString()
                    viewModel.exchangeCurrency(amountText, args.toCurrency, args.fromCurrency)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun initUI() {
        binding.tvAmount.text = args.fromCurrencyAmount
        when (args.toCurrency) {
            "USD" -> {
                binding.tvCurrenciesSymbol.text = "$"
            }
            "EUR" -> {
                binding.tvCurrenciesSymbol.text = "€"
            }
            "GBP" -> {
                binding.tvCurrenciesSymbol.text = "£"
            }
            "RUB" -> {
                binding.tvCurrenciesSymbol.text = "₽"
            }
            "CNY" -> {
                binding.tvCurrenciesSymbol.text = "¥"
            }
            else -> {
                binding.tvCurrenciesSymbol.text = "₺"
            }
        }

        when (args.fromCurrency) {
            "USD" -> {
                binding.imgSelectedCurrency.setImageResource(R.drawable.img_united_states)
                binding.tvTotalAmountSymbol.text = "$"
                binding.tvTitle.text = "${args.fromCurrency}/${args.toCurrency} Satın Alma"
                binding.tvAmount.text = "$${args.fromCurrencyAmount}"
                purchasedSymbol = "$"
            }
            "EUR" -> {
                binding.imgSelectedCurrency.setImageResource(R.drawable.img_europe)
                binding.tvTotalAmountSymbol.text = "€"
                binding.tvTitle.text = "${args.fromCurrency}/${args.toCurrency} Satın Alma"
                binding.tvAmount.text = "€${args.fromCurrencyAmount}"
                purchasedSymbol = "€"
            }
            "GBP" -> {
                binding.imgSelectedCurrency.setImageResource(R.drawable.img_united_kingdom)
                binding.tvTotalAmountSymbol.text = "£"
                binding.tvTitle.text = "${args.fromCurrency}/${args.toCurrency} Satın Alma"
                binding.tvAmount.text = "£${args.fromCurrencyAmount}"
                purchasedSymbol = "£"
            }
            "RUB" -> {
                binding.imgSelectedCurrency.setImageResource(R.drawable.img_russia)
                binding.tvTotalAmountSymbol.text = "₽"
                binding.tvTitle.text = "${args.fromCurrency}/${args.toCurrency} Satın Alma"
                binding.tvAmount.text = "₽${args.fromCurrencyAmount}"
                purchasedSymbol = "₽"
            }
            "CNY" -> {
                binding.imgSelectedCurrency.setImageResource(R.drawable.img_china)
                binding.tvTotalAmountSymbol.text = "¥"
                binding.tvTitle.text = "${args.fromCurrency}/${args.toCurrency} Satın Alma"
                binding.tvAmount.text = "¥${args.fromCurrencyAmount}"
                purchasedSymbol = "¥"
            }
            else -> {
                binding.imgSelectedCurrency.setImageResource(R.drawable.img_tr)
                binding.tvTotalAmountSymbol.text = "₺"
                binding.tvTitle.text = "${args.fromCurrency}/${args.toCurrency} Satın Alma"
                binding.tvAmount.text = "₺${args.fromCurrencyAmount}"
                purchasedSymbol = "₺"
            }
        }
    }

    private fun initData() {
        lifecycleScope.launchWhenStarted {
            viewModel.convertOperation.collect { event ->
                when (event) {
                    is ExchangeDialogViewModel.CurrencyEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvTotalAmountValue.setTextColor(Color.BLACK)
                        binding.tvTotalAmountValue.text = event.resultText
                        purchasedAmount = event.resultText
                    }
                    is ExchangeDialogViewModel.CurrencyEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvTotalAmountValue.setTextColor(Color.RED)
                        binding.tvTotalAmountValue.text = event.errorText
                    }
                    is ExchangeDialogViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}