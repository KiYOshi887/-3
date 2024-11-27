package com.example.calculatorimt

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    private val history = mutableListOf<BMIRecord>()
    private lateinit var adapter: HistoryAdapter
    private var lastReminder: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val spinnerUnits = findViewById<Spinner>(R.id.spinnerUnits)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvRecommendation = findViewById<TextView>(R.id.tvRecommendation)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerViewHistory)

        // Настройка Spinner для выбора единиц измерения
        val units = listOf("Килограммы/Метры", "Фунты/Дюймы")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUnits.adapter = spinnerAdapter

        // Настройка RecyclerView для истории
        adapter = HistoryAdapter(history)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = adapter

        // Расчёт ИМТ
        btnCalculate.setOnClickListener {
            val weightStr = etWeight.text.toString().trim()
            val heightStr = etHeight.text.toString().trim()

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Введите вес и рост!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = weightStr.toDouble()
            val height = heightStr.toDouble()
            val unitsSelected = spinnerUnits.selectedItem.toString()

            val bmi = if (unitsSelected == "Килограммы/Метры") {
                weight / (height * height)
            } else {
                703 * weight / (height * height)
            }

            tvResult.text = "Ваш ИМТ: %.2f".format(bmi)
            tvRecommendation.text = getRecommendation(bmi)

            // Сохранение в историю
            val record = BMIRecord(weight, height, unitsSelected, bmi, Date())
            history.add(0, record) // Добавляем запись в начало списка
            adapter.notifyDataSetChanged()
        }

        // Отображение истории
        btnHistory.setOnClickListener {
            recyclerViewHistory.visibility =
                if (recyclerViewHistory.visibility == RecyclerView.GONE) RecyclerView.VISIBLE else RecyclerView.GONE
        }

        // Напоминание каждые 7 дней
        if (System.currentTimeMillis() - lastReminder > 7 * 24 * 60 * 60 * 1000) {
            Toast.makeText(this, "Не забудьте обновить свои данные!", Toast.LENGTH_LONG).show()
            lastReminder = System.currentTimeMillis()
        }
    }

    private fun getRecommendation(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Вы недостаточно весите. Поговорите с врачом или диетологом."
            bmi < 24.9 -> "Ваш вес в норме. Продолжайте следить за своим здоровьем!"
            bmi < 29.9 -> "У вас избыточный вес. Рассмотрите изменения в образе жизни."
            else -> "У вас ожирение. Рекомендуется обратиться к врачу."
        }
    }
}

data class BMIRecord(
    val weight: Double,
    val height: Double,
    val units: String,
    val bmi: Double,
    val date: Date
)

class HistoryAdapter(private val history: List<BMIRecord>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): HistoryViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val record = history[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int = history.size

    class HistoryViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(record: BMIRecord) {
            text1.text = "ИМТ: %.2f (${record.units})".format(record.bmi)
            text2.text = "Дата: ${record.date}"
        }
    }
}