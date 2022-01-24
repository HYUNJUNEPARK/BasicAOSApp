package com.june.pomodorotimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import com.june.pomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindViews()

    }
    private fun bindViews() {
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    /*
                    seekBar : 이벤트가 발생한 seekBar
                    fromUser : 유저가 발생시킨 이벤트 일 때
                    progress : 유저 상관 없이 변화 생겼을 때
                    */
                    if (fromUser) {
                        val initialMills = progress * 60 * 1000L
                        updateRemainTime(initialMills)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    currentCountDownTimer?.cancel() //이미 진행중인 타이머가 있을 때 cancel
                    currentCountDownTimer = null

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {//시크바에 손을 떼었을 때
                    if (seekBar == null) {
                        return@onStopTrackingTouch
                    } else {
                        val seekBarMinutes: Int = seekBar.progress
                        val initialMills: Long = seekBarMinutes * 60 * 1000L
                        currentCountDownTimer = createCountDownTimer(initialMills)
                        currentCountDownTimer?.start()
                    }
                }
            }
        )
    }
    //See: https://developer.android.com/reference/android/os/CountDownTimer
    private fun createCountDownTimer(initialMills: Long): CountDownTimer {
        return object : CountDownTimer(initialMills, 1000L) {
            override fun onTick(millisUntilFinished: Long) {//1초마다 한번씩 호출됨
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }
            override fun onFinish() {
                updateRemainTime(0)
                updateSeekBar(0)
            }
        }
    }

    private fun updateRemainTime(remainMills: Long) {
        val remainSeconds = remainMills / 1000
        binding.remainMinutesTextView.text = "%02d".format(remainSeconds / 60)
        binding.remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMills: Long){
        val remainMinutes: Int = (remainMills / 1000 / 60).toInt()
        binding.seekBar.progress =  remainMinutes
    }
}