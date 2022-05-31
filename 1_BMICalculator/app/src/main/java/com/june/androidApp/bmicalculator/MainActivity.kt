package com.june.androidApp.bmicalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.june.androidApp.bmicalculator.databinding.ActivityMainBinding
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun initButton(v: View) {
        Log.d("testLog", "initButton: tttt")
        val height = binding.heightEditText.text
        val weight = binding.weightEditText.text

        if (height.isEmpty() || weight.isEmpty()) {
            Log.d("testLog", "Height : $height // Weight $weight")
            Toast.makeText(this, R.string.toast, Toast.LENGTH_SHORT).show()
            return
        } else {
            bmiCalculator(height.toString().toInt(), weight.toString().toInt())
        }
    }

    private fun bmiCalculator(height: Int, weight: Int) {
        val bmiPow: Double = weight / (height / 100.0).pow(2.0)
        val bmiRound: Double = round(bmiPow * 10) / 10
        val bmiResultText = when {
            bmiRound >= 30.0 -> getString(R.string.obese)
            bmiRound >= 25.0 -> getString(R.string.overweight)
            bmiRound >= 23.0 -> getString(R.string.risk_to_overweight)
            bmiRound >= 18.5 -> getString(R.string.normal)
            else -> getString(R.string.underweight)
        }
        val bmiValue: String = bmiRound.toString()

        ResultDialog(this).resultDialog(bmiValue, bmiResultText)


        binding.heightEditText.setText(getString(R.string.initialization))
        binding.weightEditText.setText(getString(R.string.initialization))
    }
}