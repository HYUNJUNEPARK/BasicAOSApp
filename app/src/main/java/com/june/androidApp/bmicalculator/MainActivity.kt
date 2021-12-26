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

        binding.resultButton.setOnClickListener {
            //[START Null check]
            val _height = binding.heightEditText.text
            val _weight = binding.weightEditText.text
            if(_height.isEmpty() || _weight.isEmpty()){
                Toast.makeText(this,"정확한 숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //[END Null check]

            //[START BMI 계산]
            val height = _height.toString().toInt()
            val weight = _weight.toString().toInt()
            bmiCalculator(height, weight)
            //[ENd BMI 계산]

            binding.heightEditText.setText("")
            binding.weightEditText.setText("")

        }
    }

    private fun bmiCalculator(height:Int, weight:Int) {
        val __bmi = weight / (height / 100.0).pow(2.0)
        val _bmi = round(__bmi*10)/10
        val resultText = when {
            _bmi >= 30.0 -> "OBESE"
            _bmi >= 25.0 -> "OVERWEIGHT"
            _bmi >= 23.0 -> "RISK TO OVERWEIGHT"
            _bmi >= 18.5 -> "NORMAL"
            else -> "UNDERWEIGHT"
        }
        val bmi:String = _bmi.toString()
        openCloseDialog(bmi, resultText)
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

    private fun setColor(resultText: String, bmiResult: TextView){
        when(resultText) {
            "OBESE" -> bmiResult.setTextColor(Color.parseColor("#FF0000"))
            "OVERWEIGHT" ->bmiResult.setTextColor(Color.parseColor("#FF8000"))
            "RISK TO OVERWEIGHT" ->bmiResult.setTextColor(Color.parseColor("#FFFF00"))
            "NORMAL" ->bmiResult.setTextColor(Color.parseColor("#00FF00"))
            else ->  bmiResult.setTextColor(Color.parseColor("#00FFFF"))
        }
    }
}