package com.june.secretdiary

import android.content.Context
import android.content.Intent
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

        //[START Setting numberPicker]
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
        //[END Setting numberPicker]

        //SharedPreferences 생성
        val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

        binding.openButton.setOnClickListener {
            //[START 예외처리]
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //[END 예외처리]

            //[START 비밀번호 확인 -> 페이지 이동 or AlertDialog]
            val passwordFromUser = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"
            /* SharedPreferences 에 저장된 값 꺼내오기
            SharedPreferences 객체.get데이터타입("SharedPreferences 이름", "default value") */
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showAlertDialog()
            }
            //[END 비밀번호 확인 -> 페이지 이동 or AlertDialog]
        }

        binding.changePasswordButton.setOnClickListener {
            val passwordFromUser = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"
            //changePasswordMode 활성화(true) -> 비밀번호 변경 가능 -> changePasswordMode 비활성화(false)
            if (changePasswordMode) {
                /* SharedPreferences 값 수정
                - edit 함수에 람다로 SharedPreferences 값 수정 가능
                - commit() : 동기적 저장 (파일이 다 저장될때까지 UI를 멈추고 기다림) - 가벼운 작업에서 사용
                - apply() : 비동기적 저장 */
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                binding.changePasswordButton.setBackgroundColor(Color.BLACK)
                initializeNumberPicker()
                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            //changePasswordMode 비활성화(false) -> changePasswordMode 활성화(true)
            } else {
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    binding.changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    showAlertDialog()
                }
            }
        }
    }
    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("Page Open Error")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ ->
                initializeNumberPicker()
            }
            .create()
            .show()
    }
    private fun initializeNumberPicker(){
        binding.numberPicker1.value = 0
        binding.numberPicker2.value = 0
        binding.numberPicker3.value = 0
    }
}