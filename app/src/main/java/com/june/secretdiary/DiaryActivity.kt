package com.june.secretdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june.secretdiary.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}