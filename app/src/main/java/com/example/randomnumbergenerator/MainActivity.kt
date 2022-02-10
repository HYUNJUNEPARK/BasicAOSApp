package com.example.randomnumbergenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.randomnumbergenerator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var checkerPressedCNB: Boolean = false //CNB(CreateNumberButton)
    private val pickedNumberSet = hashSetOf<Int>() //choiceNumberButton 버튼으로 숫자 생성하고 createButton 버튼을 눌렀을 때 중복을 피함(mutableSetOf<Int>()를 사용해도 됨)
    private val chosenNumberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            binding.chosenNumberOne, binding.chosenNumberTwo, binding.chosenNumberThree,
            binding.chosenNumberFour, binding.chosenNumberFive, binding.chosenNumberSix
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNumberPicker()
        initCreateNumberButton()
        initChoiceNumberButton()
        initClearButton()
    }

    private fun initNumberPicker() {
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 45
    }

    private fun initCreateNumberButton() {
        binding.createNumberButton.setOnClickListener {
            val numberList:Set<Int> = makeRandomNumber() //Alg 2. Set<Int> -> List<Int>
            checkerPressedCNB = true

            numberList.forEachIndexed { index, number ->
                val chosenNumberTextView = chosenNumberTextViewList[index]
                chosenNumberTextView.isVisible = true
                setCircleBackgroundColor(number, chosenNumberTextView)
                chosenNumberTextView.text = number.toString()
            }
        }
    }

    private fun makeRandomNumber(): Set<Int> { //Alg 2. Set<Int> -> List<Int>
        //Alg 1. set 을 이용해 랜덤 숫자 생성
        val numberSet = mutableSetOf<Int>()

        for (i in pickedNumberSet) {
            numberSet.add(i)
        }
        while(numberSet.size < 6) {
            val rangeNumber = (1..45).random()
            numberSet.add(rangeNumber)
        }
        return numberSet

        //Alg 2. list 를 이용해 램덤 숫자 생성
        //사용자가 미리 뽑은 값에 따라 최소 39개(6개 숫자 선택)에서 최대 45개(선택 값 없음) 랜덤한 수를 갖는 리스트가 생성
//        val randomNumberList = mutableListOf<Int>()
//            .apply {
//                for (i in 1..45) {
//                    if(pickedNumberSet.contains(i)) {
//                        continue
//                    }
//                    this.add(i)
//                }
//            }
//        randomNumberList.shuffle()
//
//        val pickedNumberList: List<Int> = pickedNumberSet.toList()
//        val restNumberList: List<Int> = randomNumberList.subList(0, 6 - pickedNumberSet.size)
//        val sixNumberList = pickedNumberList + restNumberList
//        return sixNumberList.sorted() //오름차순 정렬
    }

    private fun setCircleBackgroundColor(number:Int, chosenNumberTextView: TextView){
        when(number) {
            in 1..10 -> chosenNumberTextView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 11..20 -> chosenNumberTextView.background = ContextCompat.getDrawable(this, R.drawable.circle_orange)
            in 21..30 -> chosenNumberTextView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 31..40 -> chosenNumberTextView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
            else -> chosenNumberTextView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
        }
    }

    private fun initClearButton() {
        binding.clearButton.setOnClickListener {
            checkerPressedCNB = false
            pickedNumberSet.clear()
            chosenNumberTextViewList.forEach {
                it.isVisible = false
            }
        }
    }

    private fun initChoiceNumberButton() {
        binding.choiceNumberButton.setOnClickListener {
            //[START 예외처리]
            if(checkerPressedCNB){ //이미 createButton 버튼을 눌러 6개 번호를 얻었다면 토스트 메세지를 띄움
                Toast.makeText(this, R.string.toast_initialize, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickedNumberSet.size >= 5) {
                Toast.makeText(this, R.string.toast_max, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickedNumberSet.contains(binding.numberPicker.value)){
                Toast.makeText(this, R.string.toast_overlap, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //[END 예외처리]

            //[START NumberPicker 값 UI 세 팅]
            val chosenNumberTextView = chosenNumberTextViewList[pickedNumberSet.size]
            chosenNumberTextView.isVisible = true
            chosenNumberTextView.text = binding.numberPicker.value.toString()
            setCircleBackgroundColor(binding.numberPicker.value, chosenNumberTextView)
            pickedNumberSet.add(binding.numberPicker.value)
            //[END NumberPicker 값 UI 세팅]
        }
    }
}