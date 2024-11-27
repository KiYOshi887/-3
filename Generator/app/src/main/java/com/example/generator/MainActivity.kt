package com.example.generator

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val generatedNumbers = mutableListOf<Int>()
    private lateinit var adapter: HistogramAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMinValue = findViewById<EditText>(R.id.etMinValue)
        val etMaxValue = findViewById<EditText>(R.id.etMaxValue)
        val etStep = findViewById<EditText>(R.id.etStep)
        val etExclude = findViewById<EditText>(R.id.etExclude)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val btnRandomRange = findViewById<Button>(R.id.btnRandomRange)
        val tvResults = findViewById<TextView>(R.id.tvResults)
        recyclerView = findViewById(R.id.recyclerView)

        // Настройка RecyclerView для гистограммы
        adapter = HistogramAdapter(generatedNumbers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Генерация чисел
        btnGenerate.setOnClickListener {
            val min = etMinValue.text.toString().toIntOrNull() ?: 0
            val max = etMaxValue.text.toString().toIntOrNull() ?: 100
            val step = etStep.text.toString().toIntOrNull() ?: 1
            val excludeList = etExclude.text.toString()
                .split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .toSet()

            if (min >= max || step <= 0) {
                Toast.makeText(this, "Неверные значения диапазона или шага", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            generatedNumbers.clear()
            for (i in min..max step step) {
                if (i !in excludeList) {
                    generatedNumbers.add(i)
                }
            }
            if (generatedNumbers.isEmpty()) {
                Toast.makeText(this, "Нет доступных чисел для генерации", Toast.LENGTH_SHORT).show()
            } else {
                tvResults.text = "Результаты: $generatedNumbers"
                adapter.notifyDataSetChanged()
            }
        }

        // Генерация случайного диапазона
        btnRandomRange.setOnClickListener {
            val randomMin = Random.nextInt(1, 50)
            val randomMax = Random.nextInt(51, 100)
            etMinValue.setText(randomMin.toString())
            etMaxValue.setText(randomMax.toString())
            Toast.makeText(
                this,
                "Случайный диапазон: $randomMin - $randomMax",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

// Адаптер для гистограммы
class HistogramAdapter(private val numbers: List<Int>) :
    RecyclerView.Adapter<HistogramAdapter.HistogramViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): HistogramViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return HistogramViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistogramViewHolder, position: Int) {
        val number = numbers[position]
        holder.bind(number)
    }

    override fun getItemCount(): Int = numbers.size

    class HistogramViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(number: Int) {
            textView.text = number.toString()
        }
    }
}