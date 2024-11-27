package com.example.timerapp

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTimeInput: EditText = findViewById(R.id.etTimeInput)
        val btnStart: Button = findViewById(R.id.btnStart)
        val btnPause: Button = findViewById(R.id.btnPause)
        val btnReset: Button = findViewById(R.id.btnReset)
        val tvTimer: TextView = findViewById(R.id.tvTimer)

        btnStart.setOnClickListener {
            val input = etTimeInput.text.toString()
            if (input.isNotEmpty()) {
                val timeInSeconds = input.toLong() * 1000
                startTimer(timeInSeconds, tvTimer)
            } else {
                Toast.makeText(this, "Введите время!", Toast.LENGTH_SHORT).show()
            }
        }

        btnPause.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                resumeTimer(tvTimer)
            }
        }

        btnReset.setOnClickListener {
            resetTimer(tvTimer)
        }
    }

    private fun startTimer(timeInMillis: Long, tvTimer: TextView) {
        timeLeftInMillis = timeInMillis
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(tvTimer)
            }

            override fun onFinish() {
                isTimerRunning = false
                Toast.makeText(this@MainActivity, "Таймер завершён!", Toast.LENGTH_SHORT).show()
            }
        }.start()
        isTimerRunning = true
    }

    private fun pauseTimer() {
        timer.cancel()
        isTimerRunning = false
    }

    private fun resumeTimer(tvTimer: TextView) {
        startTimer(timeLeftInMillis, tvTimer)
    }

    private fun resetTimer(tvTimer: TextView) {
        timer.cancel()
        isTimerRunning = false
        timeLeftInMillis = 0
        tvTimer.text = "00:00"
    }

    private fun updateTimerText(tvTimer: TextView) {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }
}
