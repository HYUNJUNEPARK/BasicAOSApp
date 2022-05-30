package com.june.androidApp.bmicalculator

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView

class ResultDialog(private val context: Context) {
    fun resultDialog(bmi: String, bmiResultText: String) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
        val mDialogBuilder = AlertDialog.Builder(context).setView(mDialogView)

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
            context.getString(R.string.obese) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.red)}"))
            context.getString(R.string.overweight) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.orange)}"))
            context.getString(R.string.risk_to_overweight) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.yellow)}"))
            context.getString(R.string.normal) -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.green)}"))
            else -> bmiResultTextView.setTextColor(Color.parseColor("#${context.getString(R.string.black)}"))
        }
    }
}