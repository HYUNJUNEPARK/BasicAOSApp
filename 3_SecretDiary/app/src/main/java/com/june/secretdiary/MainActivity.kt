package com.june.secretdiary

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
    private var changePasswordMode = false //true 일 때만 PW 변경 가능
    private var passWord = "000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val passwordPreferences: SharedPreferences = getSharedPreferences("password", MODE_PRIVATE) //SharedPreferences 생성

        initPassWord(passwordPreferences)
        initOpenButton(passwordPreferences)
        initChangePWButton(passwordPreferences)
        setHomeBtnActionBar()
        initNumberPickers()
    }

    private fun initPassWord(passwordPreferences : SharedPreferences) {
        passwordPreferences.edit(true) { putString("password", passWord) }
    }

    private fun initOpenButton(passwordPreferences : SharedPreferences) {
        binding.openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, R.string.changing_pw, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userInputPW = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            if (passwordPreferences.getString("password", "000") == userInputPW) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showAlertDialog()
            }
        }
    }

    private fun initChangePWButton(passwordPreferences : SharedPreferences) {
        binding.changePasswordButton.setOnClickListener {
            passWord = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"
            if (changePasswordMode) {
                passwordPreferences.edit(true) { putString("password", passWord) }
                changePasswordMode = false
                binding.changePasswordButton.setBackgroundColor(Color.BLACK)
                initializeNumberPicker()
                Toast.makeText(this, R.string.changed_pw, Toast.LENGTH_SHORT).show()
            } else {
                if (passwordPreferences.getString("password", "000") == passWord) {
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