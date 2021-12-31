package com.example.randomnumbergenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.randomnumbergenerator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var checker: Boolean = false //createButton 버튼이 이미 눌렸는지 확인
    private val pickedNumberSet = hashSetOf<Int>() //chooseNumberButton 버튼으로 숫자 생성하고 createButton 버튼을 눌렀을 때 중복을 피함(mutableSetOf<Int>()를 사용해도 됨)
    private val listOfChosenNumberTextView: List<TextView> by lazy {
        listOf<TextView>(
            binding.chosenNumberOne,
            binding.chosenNumberTwo,
            binding.chosenNumberThree,
            binding.chosenNumberFour,
            binding.chosenNumberFive,
            binding.chosenNumberSix
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //[START NumberPicker 번호 세팅]
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 45
        //[END NumberPicker 번호 세팅]

        binding.createButton.setOnClickListener {
            val numberList:List<Int> = makeRandomNumber()
            checker = true
            numberList.forEachIndexed { index, number ->
                val chosenNumberTextView = listOfChosenNumberTextView[index]
                chosenNumberTextView.isVisible = true
                chosenNumberTextView.text = number.toString()
            }
        }
        binding.clearButton.setOnClickListener {
            checker = false
            pickedNumberSet.clear()
            listOfChosenNumberTextView.forEach {
                it.isVisible = false
            }
        }
        binding.chooseNumberButton.setOnClickListener {
            //[START 예외처리]
            if(checker){ //이미 createButton 버튼을 눌러 6개 번호를 얻었다면 토스트 메세지를 띄움
                Toast.makeText(this, "초기화 후 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickedNumberSet.size >= 5) {
                Toast.makeText(this, "지정 번호는 최대 5개까지 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickedNumberSet.contains(binding.numberPicker.value)){
                Toast.makeText(this, "이미 지정된 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //[END 예외처리]

            //[START NumberPicker 값 UI 세팅]
            val chosenNumberTextView = listOfChosenNumberTextView[pickedNumberSet.size]
            chosenNumberTextView.isVisible = true
            chosenNumberTextView.text = binding.numberPicker.value.toString()
            pickedNumberSet.add(binding.numberPicker.value)
            //[END NumberPicker 값 UI 세팅]
        }
    }

    private fun makeRandomNumber(): List<Int> {
        //[START 방법1. set 을 이용해 랜덤 숫자 생성]
//        val numberSet = mutableSetOf<Int>()
//        while(numberSet.size < 6) {
//            val rangeNumber = (1..45).random()
//            numberSet.add(rangeNumber)
//        }
//        return numberSet
        //[END 방법1. set 을 이용해 랜덤 숫자 생성]

        //[START 방법2. list 를 이용해 램덤 숫자 생성]
        val _numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if(pickedNumberSet.contains(i)) {
                        continue
                    }
                    this.add(i)
                }
            }
        _numberList.shuffle()
        val numberList = pickedNumberSet.toList() + _numberList.subList(0, 6 - pickedNumberSet.size)
        return numberList.sorted()
        //[END 방법2. list 를 이용해 램덤 숫자 생성]
    }
}