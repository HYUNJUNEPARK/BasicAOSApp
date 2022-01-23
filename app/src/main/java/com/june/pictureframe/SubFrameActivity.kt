package com.june.pictureframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june.pictureframe.databinding.ActivitySubFrameBinding
import java.util.*
import kotlin.concurrent.timer

class SubFrameActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySubFrameBinding.inflate(layoutInflater) }
    private val photoList = mutableListOf<Uri>()
    private var currentPosition = 0
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getPhotoUriFrameIntent()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun getPhotoUriFrameIntent() {
        val photoListSize = intent.getIntExtra("photoListSize", 0)
        for (i in 0..photoListSize) {
            intent.getStringExtra("photo$i")?.let { uri_str ->
                photoList.add(Uri.parse(uri_str))
            }
        }
    }

    private fun startTimer(){
        timer = timer(period = 5000) {
            runOnUiThread {
                val currentSlidePosition = currentPosition
                val nextSlidePosition = if (photoList.size <= currentPosition + 1) {
                    0//슬라이드 인덱스 넘어가면 초기화
                } else {
                    currentPosition + 1//다음 슬라이드 인덱스
                }
                //frontImageView 투명 -> backImageView 이미지 보임
                binding.backImageView.setImageURI(photoList[currentSlidePosition])
                binding.frontImageView.alpha = 0f//투명
                binding.frontImageView.setImageURI(photoList[nextSlidePosition])
                binding.frontImageView.animate()
                    .alpha(1.0f)//불투명
                    .setDuration(1000)
                    .start()
                currentPosition = nextSlidePosition
            }
        }
    }
}