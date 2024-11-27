package com.example.lumus

import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isFlashlightOn = false
    private var isBlinking = false
    private var isSOS = false
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]

        val btnToggleFlashlight = findViewById<Button>(R.id.btnToggleFlashlight)
        val btnBlinkFlashlight = findViewById<Button>(R.id.btnBlinkFlashlight)
        val btnSOS = findViewById<Button>(R.id.btnSOS)
        val seekBarBrightness = findViewById<SeekBar>(R.id.seekBarBrightness)
        val btnScreenLight = findViewById<Button>(R.id.btnScreenLight)

        // Включение/выключение фонарика
        btnToggleFlashlight.setOnClickListener {
            toggleFlashlight()
        }

        // Режим мигания
        btnBlinkFlashlight.setOnClickListener {
            if (isBlinking) {
                isBlinking = false
            } else {
                isBlinking = true
                blinkFlashlight()
            }
        }

        // Режим SOS
        btnSOS.setOnClickListener {
            if (isSOS) {
                isSOS = false
            } else {
                isSOS = true
                sosFlashlight()
            }
        }

        // Управление яркостью
        seekBarBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setFlashlightBrightness(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Экран как фонарик
        btnScreenLight.setOnClickListener {
            Toast.makeText(this, "Функция экрана-фонарика не реализована", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFlashlight() {
        try {
            isFlashlightOn = !isFlashlightOn
            cameraManager.setTorchMode(cameraId, isFlashlightOn)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun blinkFlashlight() {
        val handler = Handler(Looper.getMainLooper())
        val blinkRunnable = object : Runnable {
            override fun run() {
                if (isBlinking) {
                    toggleFlashlight()
                    handler.postDelayed(this, 500)
                }
            }
        }
        handler.post(blinkRunnable)
    }

    private fun sosFlashlight() {
        val handler = Handler(Looper.getMainLooper())
        val sosRunnable = object : Runnable {
            var sosPattern = listOf(200L, 200L, 200L, 600L, 600L, 600L, 200L, 200L, 200L)
            var index = 0

            override fun run() {
                if (isSOS) {
                    toggleFlashlight()
                    handler.postDelayed(this, sosPattern[index])
                    index = (index + 1) % sosPattern.size
                }
            }
        }
        handler.post(sosRunnable)
    }

    private fun setFlashlightBrightness(brightness: Int) {
        // Временное использование параметра
        println("Уровень яркости установлен на $brightness")
        Toast.makeText(this, "Яркость: $brightness (функция не поддерживается)", Toast.LENGTH_SHORT).show()
    }

}