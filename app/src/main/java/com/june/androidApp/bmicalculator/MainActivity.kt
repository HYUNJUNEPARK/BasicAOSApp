package com.june.androidApp.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.june.androidApp.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.resultButton.setOnClickListener {
            val _height = binding.heightEditText.text
            val _weight = binding.weightEditText.text

            if(_height.isEmpty() || _weight.isEmpty()){
                Toast.makeText(this,"정확한 숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height = _height.toString().toInt()
            val weight = _weight.toString().toInt()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("height", height)
            intent.putExtra("weight", weight)

            startActivity(intent)
        }
    }
}