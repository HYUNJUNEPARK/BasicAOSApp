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
import com.june.androidApp.bmicalculator.databinding.CustomDialogBinding
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val bindingDialog by lazy { CustomDialogBinding.inflate(layoutInflater) }

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
        //[START 팝업 세팅&열기]
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)

//        bindingDialog.tvBmiValue.text = bmi /*bmi 값 UI 세팅*/
        val bmiValue = mDialogView.findViewById<TextView>(R.id.tv_bmi_value)
        bmiValue.text = bmi

        /*bmi 결과 UI 세팅*/
//      bindingDialog.tvBmiResult.text = resultText

        val bmiResult = mDialogView.findViewById<TextView>(R.id.tv_bmi_result)
        setBmiTextColor(bmiResultText, bmiResult)
        bmiResult.text = bmiResultText
        /*팝업 열기*/

        val mAlertDialog = mBuilder.show()
        //[END 팝업 세팅&열기]

        //[START 팝업 닫기 버튼]
        val closeBtn = mDialogView.findViewById<Button>(R.id.btn_close)
        closeBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun setBmiTextColor(bmiResultText: String, bmiResult: TextView) {
        when (bmiResultText) {
            getString(R.string.obese) -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.red)}"))
            getString(R.string.overweight) -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.orange)}"))
            getString(R.string.risk_to_overweight) -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.yellow)}"))
            getString(R.string.normal) -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.green)}"))
            else -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.black)}"))
        }
    }
}