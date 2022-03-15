package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var isOperator = false
    private var hasOperator = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun buttonClicked(view: View) {
        when(view.id) {
            R.id.button0 -> numberButtonClicked("0")
            R.id.button1 -> numberButtonClicked("1")
            R.id.button2 -> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")
            R.id.buttonPlus -> operatorButtonClicked("+")
            R.id.buttonMinus -> operatorButtonClicked("-")
            R.id.buttonModulo -> operatorButtonClicked("%")
            R.id.buttonMulti -> operatorButtonClicked("*")
            R.id.buttonDivider -> operatorButtonClicked("/")
        }
    }

    private fun numberButtonClicked(number: String) {
        if (isOperator) {
            binding.expressionTextView.append(" ")
        }
        isOperator = false

        val expressionText = binding.expressionTextView.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        else if(expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        binding.expressionTextView.append(number)

        //TODO resultTextView 실시간으로 계산 결과를 넣어야하는 기능

    }

    private fun operatorButtonClicked(operator: String) {
        if (binding.expressionTextView.text.isEmpty()) {
            return
        }
        when {
            isOperator -> {
              val text = binding.expressionTextView.text.toString()
              binding.expressionTextView.text = text.dropLast(1) + operator  //droplast
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.expressionTextView.append(" $operator")
            }
        }

        //spannalbeStringBuilder
        val ssb = SpannableStringBuilder(binding.expressionTextView.text)

        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            binding.expressionTextView.text.length -1,
            binding.expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.expressionTextView.text = ssb

        isOperator = true
        hasOperator = true

    }

    fun clearButtonClicked(view: View) {

    }



    fun resultButtonClicked(view: View) {

    }

    fun historyButtonClicked(view: View) {

    }
}