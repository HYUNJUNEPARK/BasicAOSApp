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
                Toast.makeText(this, "정확한 숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
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
        val bmiPow = weight / (height / 100.0).pow(2.0)
        val bmiRound = round(bmiPow * 10) / 10
        val resultText = when {
            bmiRound >= 30.0 -> "OBESE"
            bmiRound >= 25.0 -> "OVERWEIGHT"
            bmiRound >= 23.0 -> "RISK TO OVERWEIGHT"
            bmiRound >= 18.5 -> "NORMAL"
            else -> "UNDERWEIGHT"
        }
        val bmi: String = bmiRound.toString()
        openCloseDialog(bmi, resultText)
        binding.heightEditText.setText("")
        binding.weightEditText.setText("")
    }

    private fun openCloseDialog(bmi: String, resultText: String) {
        //[START 팝업 세팅&열기]
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        /*bmi 값 UI 세팅*/
        val bmiValue = mDialogView.findViewById<TextView>(R.id.tv_bmi_value)
        bmiValue.text = bmi
        /*bmi 결과 UI 세팅*/
        val bmiResult = mDialogView.findViewById<TextView>(R.id.tv_bmi_result)
        setColor(resultText, bmiResult)
        bmiResult.text = resultText
        /*팝업 열기*/
        val mAlertDialog = mBuilder.show()
        //[START 팝업 세팅&열기]

        //[START 팝업 닫기 버튼]
        val closeBtn = mDialogView.findViewById<Button>(R.id.btn_confirm)
        closeBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }
        //[END 팝업 닫기 버튼]
    }

    private fun setColor(resultText: String, bmiResult: TextView) {
        when (resultText) {
            "OBESE" -> bmiResult.setTextColor(Color.parseColor("#FF0000"))
            "OVERWEIGHT" -> bmiResult.setTextColor(Color.parseColor("#FF8000"))
            "RISK TO OVERWEIGHT" -> bmiResult.setTextColor(Color.parseColor("#FFFF00"))
            "NORMAL" -> bmiResult.setTextColor(Color.parseColor("#00FF00"))
            else -> bmiResult.setTextColor(Color.parseColor("#00FFFF"))
        }
    }
}