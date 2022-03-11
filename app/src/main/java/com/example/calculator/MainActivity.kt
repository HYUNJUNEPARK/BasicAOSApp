package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    fun buttonClicked(view: View) {

    }

    fun clearButtonClicked(view: View) {

    }
    fun historyButtonClicked(view: View) {

    }
    fun resultButtonClicked(view: View) {

    }
}