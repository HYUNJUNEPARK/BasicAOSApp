package com.june.pictureframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.june.pictureframe.databinding.ActivitySubFrameBinding
import kotlin.concurrent.timer

class SubFrameActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySubFrameBinding.inflate(layoutInflater) }
    private val photoList = mutableListOf<Uri>()
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //TODO 사진 선택이 안되어있으면 앱 크래쉬 발생
        getPhotoUriFrameIntent()
        startTimer()

    }

    private fun getPhotoUriFrameIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let { uri_string ->
                photoList.add(Uri.parse(uri_string))
            }
        }
    }

    private fun startTimer(){
        timer(period = 5000) {
            runOnUiThread {
                //TODO : 변수 이름 바꾸기
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                binding.backView.setImageURI(photoList[current])
                //frontView 투명 -> backView 이미지 보임
                binding.frontView.alpha = 0f
                binding.frontView.setImageURI(photoList[next])
                binding.frontView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }
}