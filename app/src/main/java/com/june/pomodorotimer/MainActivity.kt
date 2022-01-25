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

        initSeekBar()
    }

    //See: https://developer.android.com/reference/android/widget/SeekBar.OnSeekBarChangeListener
    private fun initSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {//Notification that the user has started a touch gesture
                    //이미 진행중인 타이머가 있을 때 cancel
                    currentCountDownTimer?.cancel()
                    currentCountDownTimer = null
                }
                override fun onProgressChanged(seekBar: SeekBar?, progressMinute: Int, fromUser: Boolean) {//Notification that the progress level has changed
                    if (fromUser) {
                        val remainMills = progressMinute * 60 * 1000L
                        updateUIbyRemainTime(remainMills)
                    }
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {//Notification that the user has finished a touch gesture
                    if (seekBar == null) {
                        return
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
        return object : CountDownTimer(initialMills, 1000L) {//countDownInterval 마다 onTick 호출 -> UI 세팅
            override fun onTick(millisUntilFinished: Long) {
                updateUIbyRemainTime(millisUntilFinished)
                updateSeekBarUI(millisUntilFinished)
            }
            override fun onFinish() {
                updateUIbyRemainTime(0)
                updateSeekBarUI(0)
            }
        }
    }

    private fun updateUIbyRemainTime(remainMills: Long) {
        val remainSeconds = remainMills / 1000
        binding.remainMinutesTextView.text = "%02d".format(remainSeconds / 60)
        binding.remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBarUI(remainMills: Long){
        val remainMinutes: Int = (remainMills / 1000 / 60).toInt()
        binding.seekBar.progress =  remainMinutes
    }
}