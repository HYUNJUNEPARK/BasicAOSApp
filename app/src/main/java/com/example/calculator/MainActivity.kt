package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding
import java.lang.NumberFormatException

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
        binding.resultTextView.text = calculateExpression()


    }

    private fun operatorButtonClicked(operator: String) {
        if (binding.expressionTextView.text.isEmpty()) {
            return
        }
        when {
            isOperator -> {
              val text = binding.expressionTextView.text.toString()
                //dropLast
              binding.expressionTextView.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.expressionTextView.append(" $operator")
            }
        }

        //SpannableStringBuilder
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

    private fun calculateExpression(): String {
        val expressionTexts = binding.expressionTextView.text.split(" ")
        if (hasOperator.not() || expressionTexts.size !=3) {
            return ""
        }
        else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val operator = expressionTexts[1]

        return when (operator) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(view: View) {

    }
}

/*
 확장함수
 객체.확장하려고하는 함수
 expressionTexts[0] 은 string 인데 isNumber 를 쓰지 못함
 함수를 확장해서 사용할 수 있도록함
*/
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    }
    catch (e: NumberFormatException) {
        false
    }
}