package com.june.secretdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.june.secretdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var changePasswordMode = false //비밀번호를 수정하는 동안 다른 작업을 할 수 없게 하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE) //SharedPreferences 생성

        setHomeBtnActionBar()
        initNumberPickers()
        initOpenButton(passwordPreferences)
        initChangePWButton(passwordPreferences)
    }


    private fun initOpenButton(passwordPreferences : SharedPreferences) {
        binding.openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, R.string.changing_pw, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userInputPW = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            /* SharedPreferences 에 저장된 값 꺼내오기
            SharedPreferences 객체.get데이터타입("SharedPreferences 이름", "default value") */
            if (passwordPreferences.getString("password", "000").equals(userInputPW)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showAlertDialog()
            }

        }
    }

    private fun initChangePWButton(passwordPreferences : SharedPreferences) {
        binding.changePasswordButton.setOnClickListener {
            val passwordFromUser = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"
            //changePasswordMode 활성화(true) -> 비밀번호 변경 가능 -> changePasswordMode 비활성화(false)
            if (changePasswordMode) {
                /*SharedPreferences 값 수정
                - edit 함수에 람다로 SharedPreferences 값 수정 가능
                - commit() : 동기적 저장 (파일이 다 저장될때까지 UI를 멈추고 기다림) - 가벼운 작업에서 사용
                - apply() : 비동기적 저장 */
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                binding.changePasswordButton.setBackgroundColor(Color.BLACK)
                initializeNumberPicker()
                Toast.makeText(this, R.string.changed_pw, Toast.LENGTH_SHORT).show()
                //changePasswordMode 비활성화(false) -> changePasswordMode 활성화(true)
            } else {
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, R.string.insert_pw, Toast.LENGTH_SHORT).show()
                    binding.changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    showAlertDialog()
                }
            }
        }
    }

    private fun initNumberPickers() {
        binding.numberPicker1.apply {
            minValue = 0
            maxValue = 9
        }
        binding.numberPicker2.apply {
            minValue = 0
            maxValue = 9
        }
        binding.numberPicker3.apply {
            minValue = 0
            maxValue = 9
        }
    }

    private fun initializeNumberPicker(){
        binding.numberPicker1.value = 0
        binding.numberPicker2.value = 0
        binding.numberPicker3.value = 0
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.wrong_pw)
            .setPositiveButton(R.string.confirm) { _, _ ->
                initializeNumberPicker()
            }
            .create()
            .show()
    }


    private fun setHomeBtnActionBar(){
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}