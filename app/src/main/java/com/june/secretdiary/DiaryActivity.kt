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
    private val handler = Handler(Looper.getMainLooper()) //메인 스레드에 연결된 핸들러


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //SharedPreferences 생성 & Preference 저장된 값 가져오기
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)
        binding.diraryEditText.setText(detailPreferences.getString("detail", ""))

        val runnable = Runnable {
            //비동기적 저장
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit(false) {
                putString("detail", binding.diraryEditText.text.toString())
            }
        }

        //텍스트 수정을 잠시 멈추면(500ms) 자동 저장
        binding.diraryEditText.addTextChangedListener {
            handler.removeCallbacks(runnable) //postDelayed() 가 실행되기 전에 runnable 이 있다면 지워줌
            handler.postDelayed(runnable, 500) //500ms 에 한번씩 runnable 호출
        }
    }
}