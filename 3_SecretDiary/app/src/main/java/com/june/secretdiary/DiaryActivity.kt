package com.june.secretdiary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.june.secretdiary.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    private val handler = Handler(Looper.getMainLooper()) //메인 스레드에 연결된 핸들러로 Runnable 객체를 처리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createPreferencesAndUI()
        initInitializeButton()
        createRunnable()
    }

    private fun createPreferencesAndUI() {
        val diaryPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)
        binding.diaryEditText.setText(diaryPreferences.getString("diary", ""))
    }

    private fun createRunnable() {
        val saveRunnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit(false) {
                putString("diary", binding.diaryEditText.text.toString())
            }
        }
        autoSave(saveRunnable)
    }

    private fun autoSave(saveRunnable : Runnable) {
        binding.diaryEditText.addTextChangedListener {
            handler.removeCallbacks(saveRunnable) //postDelayed() 가 실행되기 전에 runnable 이 있다면 지움
            handler.postDelayed(saveRunnable, 500) //500ms 에 한번씩 runnable 호출
        }
    }

    private fun initInitializeButton() {
        binding.initializeButton.setOnClickListener {
            binding.diaryEditText.setText("")
        }
    }
}