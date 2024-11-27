package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var exchangeRateInput: EditText
    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var convertButton: Button
    private lateinit var updateRateButton: Button
    private lateinit var resultText: TextView

    private val exchangeRates = mutableMapOf<String, Double>() // Курсы валют

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация компонентов
        amountInput = findViewById(R.id.amountInput)
        exchangeRateInput = findViewById(R.id.exchangeRateInput)
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner)
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner)
        convertButton = findViewById(R.id.convertButton)
        updateRateButton = findViewById(R.id.updateRateButton)
        resultText = findViewById(R.id.resultText)

        // Инициализация курсов валют
        initializeExchangeRates()

        // Настройка спиннеров
        val currencies = listOf("USD", "EUR", "RUB", "BTC", "ETH")
        val currencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromCurrencySpinner.adapter = currencyAdapter
        toCurrencySpinner.adapter = currencyAdapter

        // Обработчики кнопок
        convertButton.setOnClickListener { convertCurrency() }
        updateRateButton.setOnClickListener { updateExchangeRates() }
    }

    private fun initializeExchangeRates() {
        exchangeRates["USD_EUR"] = 0.93
        exchangeRates["EUR_USD"] = 1.07
        exchangeRates["USD_RUB"] = 74.0
        exchangeRates["RUB_USD"] = 0.013
        exchangeRates["BTC_ETH"] = 15.0
        exchangeRates["ETH_BTC"] = 0.067
    }

    private fun convertCurrency() {
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()
        val key = "${fromCurrency}_${toCurrency}"

        val amount = amountInput.text.toString().toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
            return
        }

        var exchangeRate = exchangeRates[key] ?: 0.0
        if (exchangeRateInput.text.isNotEmpty()) {
            exchangeRate = exchangeRateInput.text.toString().toDoubleOrNull() ?: exchangeRate
        }

        if (exchangeRate == 0.0) {
            Toast.makeText(this, "Курс валют не найден", Toast.LENGTH_SHORT).show()
            return
        }

        val result = amount * exchangeRate
        resultText.text = "Результат: %.2f".format(result)
    }

    private fun updateExchangeRates() {
        exchangeRates["USD_EUR"] = 0.95
        exchangeRates["EUR_USD"] = 1.05
        exchangeRates["USD_RUB"] = 75.0
        Toast.makeText(this, "Курсы валют обновлены", Toast.LENGTH_SHORT).show()
    }
}
