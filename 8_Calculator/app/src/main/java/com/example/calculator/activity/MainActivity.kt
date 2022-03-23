package com.example.calculator.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.db.AppDatabase
import com.example.calculator.model.History

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var isOperator = false
    private var hasOperator = false
    private lateinit var db: AppDatabase

    companion object {
        const val TAG = "asdf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createDB()
    }

    private fun createDB() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            getString(R.string.db_name)
        ).build()
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

        val expressionText: List<String> = binding.expressionTextView.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, getString(R.string.limit_numbers_line), Toast.LENGTH_SHORT).show()
            return
        }
        else if(expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, getString(R.string.cannot_front_0), Toast.LENGTH_SHORT).show()
            return
        }
        else {
            binding.expressionTextView.append(number)
            binding.resultTextView.text = calculateResult()
        }
    }

    private fun operatorButtonClicked(operator/* `+` `-` `*` `%` / */: String) {
        if (binding.expressionTextView.text.isEmpty()) {
            return
        }
        when {
            //수식 뒤에 연산자가 두번 온 경우 기존 연산자를 지우고 새로 입력된 연산자로 대체 ex) 35 +- -> 35 -
            isOperator -> {
                val expressionText = binding.expressionTextView.text.toString() //35 +
                binding.expressionTextView.text = expressionText.dropLast(1) + operator //expressionText.dropLast(1) : 35
            }
            //한 수식에 연산자가 2개 이상 들어간 경우 토스트 메시지 ex) 65*36+
            hasOperator -> {
                Toast.makeText(this, getString(R.string.use_operator_only_one), Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.expressionTextView.append(" $operator")
            }
        }
        setOperatorColor()
        isOperator = true
        hasOperator = true
    }

    private fun setOperatorColor() {
        val ssb = SpannableStringBuilder(binding.expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            binding.expressionTextView.text.length -1,
            binding.expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.expressionTextView.text = ssb
    }

    private fun calculateResult(): String {
        val expressionTexts: List<String> = binding.expressionTextView.text.split(" ")
        if (hasOperator.not()/* false -> true */ || expressionTexts.size !=3) {
            return ""
        }
        //fun String.isNumber(): Boolean
        else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }
        val firstNumber = expressionTexts[0].toBigInteger()
        val secondNumber = expressionTexts[2].toBigInteger()
        return when (expressionTexts[1]) {
            "+" -> (firstNumber + secondNumber).toString()
            "-" -> (firstNumber - secondNumber).toString()
            "*" -> (firstNumber * secondNumber).toString()
            "/" -> (firstNumber / secondNumber).toString()
            "%" -> (firstNumber % secondNumber).toString()
            else -> ""
        }
    }

    fun resultButtonClicked(view: View) {
        val expressionTexts: List<String> = binding.expressionTextView.text.split(" ")
        if (binding.expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }
        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, getString(R.string.make_formula), Toast.LENGTH_SHORT).show()
            return
        }
        //숫자가 아닌 오염된 데이터 필터링
        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            return
        }

        //DB
        val expressionText = binding.expressionTextView.text.toString()
        val resultText = calculateResult()
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()
        binding.resultTextView.text = ""
        binding.expressionTextView.text = resultText

        isOperator = false
        hasOperator = false
    }

    fun clearButtonClicked(view: View) {
        binding.expressionTextView.text = ""
        binding.resultTextView.text = ""
        isOperator = false
        hasOperator = false
    }

//History Function
    fun historyButtonClicked(view: View) {
        binding.mainHistoryLayout.isVisible = true
        binding.mainHistoryLinearLayout.removeAllViews()
        Thread(Runnable{
            db.historyDao().getAll().reversed().forEach { historyData ->
                runOnUiThread {
                    val historyRowBinding = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyRowBinding.findViewById<TextView>(R.id.historyExpressionTextView).text = historyData.expression
                    historyRowBinding.findViewById<TextView>(R.id.historyResultTextView).text = "= ${historyData.result}"
                    binding.mainHistoryLinearLayout.addView(historyRowBinding)
                }
            }
        }).start()
    }

    fun historyClearButtonClicked(view: View) {
        binding.mainHistoryLinearLayout.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }

    fun closeHistoryButtonClicked(view: View) {
        binding.mainHistoryLayout.isVisible = false
    }
}

//Extension Function
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    }
    catch (e: NumberFormatException) {
        false
    }
}