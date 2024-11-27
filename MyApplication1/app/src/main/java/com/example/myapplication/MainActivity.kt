package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var number1: EditText
    private lateinit var number2: EditText
    private lateinit var operationSpinner: Spinner
    private lateinit var calculateButton: Button
    private lateinit var resetButton: Button
    private lateinit var swapButton: Button
    private lateinit var resultText: TextView
    private lateinit var historyListView: ListView

    private val history = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        number1 = findViewById(R.id.number1)
        number2 = findViewById(R.id.number2)
        operationSpinner = findViewById(R.id.operationSpinner)
        calculateButton = findViewById(R.id.calculateButton)
        resetButton = findViewById(R.id.resetButton)
        swapButton = findViewById(R.id.swapButton)
        resultText = findViewById(R.id.resultText)
        historyListView = findViewById(R.id.historyListView)

        val historyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, history)
        historyListView.adapter = historyAdapter

        calculateButton.setOnClickListener {
            calculate(historyAdapter)
        }

        resetButton.setOnClickListener {
            resetFields()
        }

        swapButton.setOnClickListener {
            swapNumbers()
        }
    }

    private fun calculate(adapter: ArrayAdapter<String>) {
        val num1 = number1.text.toString().toDoubleOrNull()
        val num2 = number2.text.toString().toDoubleOrNull()
        if (num1 == null || num2 == null) {
            Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show()
            return
        }

        val operation = operationSpinner.selectedItem.toString()
        val result = when (operation) {
            "Сложение" -> num1 + num2
            "Вычитание" -> num1 - num2
            "Умножение" -> num1 * num2
            "Деление" -> if (num2 != 0.0) num1 / num2 else "Ошибка (деление на ноль)"
            "Возведение в степень" -> Math.pow(num1, num2)
            else -> "Неизвестная операция"
        }

        resultText.text = "Результат: $result"
        history.add(0, "$num1 $operation $num2 = $result")
        if (history.size > 5) history.removeLast()
        adapter.notifyDataSetChanged()
    }

    private fun resetFields() {
        number1.text.clear()
        number2.text.clear()
        resultText.text = "Результат:"
        history.clear()
        (historyListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }

    private fun swapNumbers() {
        val temp = number1.text.toString()
        number1.setText(number2.text.toString())
        number2.setText(temp)
    }
}
