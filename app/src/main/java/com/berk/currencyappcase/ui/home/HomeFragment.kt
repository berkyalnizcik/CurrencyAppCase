package com.berk.currencyappcase.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.berk.currencyappcase.R
import com.berk.currencyappcase.databinding.FragmentHomeBinding
import com.berk.currencyappcase.main.MainViewModel
import com.berk.currencyappcase.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var currenciesAdapter: CurrenciesAdapter
    private var fromCurrency = "TRY"
    private var fromCurrencyAmount = 100
    private val defaultToCurrency = listOf("USD", "EUR", "GBP", "RUB", "CNY")
    private var randomSevenDigitNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initUI()
        viewModel.convert(fromCurrencyAmount.toString(), fromCurrency, defaultToCurrency)
        getExchangeRatesFromAPI()
        initSpinner()
    }

    private fun initUI() {
        randomSevenDigitNumber = (1000000..9999999).random()
        binding.tvWalletIdValue.text = randomSevenDigitNumber.toString()
        binding.tvAmount.text = "₺$fromCurrencyAmount"
    }

    private fun onClick() {
        binding.imgCopy.setOnClickListener {
            binding.imgCopy.setOnClickListener {
                val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
                val clip = ClipData.newPlainText("wallet_id", binding.tvWalletIdValue.text)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(requireContext(), getString(R.string.copy_text), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnAddMoney.setOnClickListener {
            val randomIncrement = (0..100).random()
            fromCurrencyAmount += randomIncrement
            binding.tvAmount.text = "₺$fromCurrencyAmount"
            viewModel.convert(fromCurrencyAmount.toString(), fromCurrency, defaultToCurrency)
        }
        binding.btnPastTransactions.setOnClickListener {
            val direction =
                HomeFragmentDirections.actionHomeFragmentToPastTransactionsFragment()
            findNavController().navigate(direction)
        }
    }

    private fun getExchangeRatesFromAPI() {
        lifecycleScope.launchWhenStarted {
            viewModel.convertOperation.collect { event ->
                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        binding.progressBar.isVisible = false
                        val convertedText = event.resultText
                        currenciesAdapter = CurrenciesAdapter(convertedText)
                        initAdapter()
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        binding.progressBar.isVisible = false
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initAdapter() {
        binding.rcvCurrencies.adapter = currenciesAdapter
        binding.rcvCurrencies.layoutManager = LinearLayoutManager(requireActivity())
        currenciesAdapter.setOnItemClickListener {
            val direction =
                HomeFragmentDirections.actionHomeFragmentToExchangeDialogFragment(
                    fromCurrency,
                    it,
                    fromCurrencyAmount.toString()
                )
            findNavController().navigate(direction)
        }
    }

    private fun initSpinner() {
        binding.spFromCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    fromCurrency = binding.spFromCurrency.selectedItem as String
                    when (fromCurrency) {
                        "USD" -> {
                            binding.imgTl.setImageResource(R.drawable.img_united_states)
                            binding.tvAmount.text = "$$fromCurrencyAmount"
                        }
                        "EUR" -> {
                            binding.imgTl.setImageResource(R.drawable.img_europe)
                            binding.tvAmount.text = "€$fromCurrencyAmount"
                        }
                        "GBP" -> {
                            binding.imgTl.setImageResource(R.drawable.img_united_kingdom)
                            binding.tvAmount.text = "£$fromCurrencyAmount"
                        }
                        "RUB" -> {
                            binding.imgTl.setImageResource(R.drawable.img_russia)
                            binding.tvAmount.text = "₽$fromCurrencyAmount"
                        }
                        "CNY" -> {
                            binding.imgTl.setImageResource(R.drawable.img_china)
                            binding.tvAmount.text = "¥$fromCurrencyAmount"
                        }
                        else -> {
                            binding.imgTl.setImageResource(R.drawable.img_tr)
                            binding.tvAmount.text = "₺$fromCurrencyAmount"
                        }
                    }
                    viewModel.convert(
                        fromCurrencyAmount.toString(),
                        fromCurrency,
                        defaultToCurrency
                    )
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }
    }
}