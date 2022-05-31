package com.june.androidApp.bmicalculator

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.june.androidApp.bmicalculator.databinding.CustomDialogBinding

class ResultDialog(private val context: Context){
    private val binding = DataBindingUtil.inflate<CustomDialogBinding>(
        LayoutInflater.from(context), R.layout.custom_dialog,null,false
    )
    private val dialog = Dialog(context)

    fun resultDialog(bmi: String, bmiResultText: String) {
        binding.tvBmiValue.text = bmi
        binding.tvBmiResult.text = bmiResultText
        setBmiTextColor(bmiResultText, binding.tvBmiResult)

        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setBmiTextColor(bmiResultText: String, bmiResultTextView: TextView) {
        when (bmiResultText) {
            context.getString(R.string.obese) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.red)}"))
            context.getString(R.string.overweight) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.orange)}"))
            context.getString(R.string.risk_to_overweight) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.yellow)}"))
            context.getString(R.string.normal) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.green)}"))
            else -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.black)}"))
        }
    }
}