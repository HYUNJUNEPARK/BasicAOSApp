package com.june.androidApp.bmicalculator

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.june.androidApp.bmicalculator.databinding.ActivityMainBinding
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initButton()
    }

    private fun initButton() {
        binding.resultButton.setOnClickListener {
            if (checkUserInputNull()) {
                Toast.makeText(this, R.string.toast, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val height = binding.heightEditText.text.toString().toInt()
                val weight = binding.weightEditText.text.toString().toInt()
                bmiCalculator(height, weight)
            }
        }
    }

    private fun checkUserInputNull(): Boolean {
        val height = binding.heightEditText.text
        val weight = binding.weightEditText.text
        return height.isEmpty() || weight.isEmpty()
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
        openCloseDialog(bmiValue, bmiResultText)
        initializeUserInput()
    }

    private fun initializeUserInput() {
        binding.heightEditText.setText(getString(R.string.initialization))
        binding.weightEditText.setText(getString(R.string.initialization))
    }

    private fun openCloseDialog(bmi: String, bmiResultText: String) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mDialogBuilder = AlertDialog.Builder(this).setView(mDialogView)

        val bmiValueTextView = mDialogView.findViewById<TextView>(R.id.tv_bmi_value)
        val bmiResultTextView = mDialogView.findViewById<TextView>(R.id.tv_bmi_result)
        val closeBtn = mDialogView.findViewById<Button>(R.id.btn_close)

        bmiValueTextView.text = bmi //bmi 수치
        setBmiTextColor(bmiResultText, bmiResultTextView)
        bmiResultTextView.text = bmiResultText //bmi 결과 값

        val mAlertDialog = mDialogBuilder.show()

        closeBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun setBmiTextColor(bmiResultText: String, bmiResultTextView: TextView) {
        when (bmiResultText) {
            getString(R.string.obese) -> bmiResultTextView.setTextColor(Color.parseColor("#${getString(R.string.red)}"))
            getString(R.string.overweight) -> bmiResultTextView.setTextColor(Color.parseColor("#${getString(R.string.orange)}"))
            getString(R.string.risk_to_overweight) -> bmiResultTextView.setTextColor(Color.parseColor("#${getString(R.string.yellow)}"))
            getString(R.string.normal) -> bmiResultTextView.setTextColor(Color.parseColor("#${getString(R.string.green)}"))
            else -> bmiResultTextView.setTextColor(Color.parseColor("#${getString(R.string.black)}"))
        }
    }
}