package com.june.androidApp.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june.androidApp.bmicalculator.databinding.ActivityResultBinding
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {
    private val binding by lazy {ActivityResultBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val height = intent.getIntExtra("height", 0)
        val weight = intent.getIntExtra("weight",0)
        val bmi = weight / (height / 100.0).pow(2.0)

        val resultText = when {
            bmi >= 30.0 -> "OBESE"
            bmi >= 25.0 -> "OVERWEIGHT"
            bmi >= 23.0 -> "RISK TO OVERWEIGHT"
            bmi >= 18.5 -> "NORMAL"
            else -> "UNDERWEIGHT"
        }

        binding.bmiValue.text = bmi.toString()
        binding.bmiResult.text = resultText.toString()
    }
}