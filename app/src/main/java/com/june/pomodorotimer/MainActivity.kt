package com.june.pomodorotimer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import com.june.pomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentCountDownTimer: CountDownTimer? = null
    private val soundPool = SoundPool.Builder().build() //See: https://developer.android.com/reference/android/media/SoundPool
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSeekBar()
        initSounds()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release() //사운드 파일이 메모리에 계속 올라가 있으면 자원낭비
    }

    //See: https://developer.android.com/reference/android/widget/SeekBar.OnSeekBarChangeListener
    private fun initSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {//Notification that the user has started a touch gesture
                    //이미 진행중인 타이머가 있을 때 cancel -> null
                    stopCountDown()
                }
                override fun onProgressChanged(seekBar: SeekBar?, progressMinute: Int, fromUser: Boolean) {//Notification that the progress level has changed
                    if (fromUser) {
                        val initialMills = progressMinute * 60 * 1000L
                        updateUIbyRemainTime(initialMills)
                    }
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {//Notification that the user has finished a touch gesture
                    if (seekBar == null) return

                    if (seekBar.progress == 0) {//0에서 바로 떼면 0인 상태에서 타이머가 실행되는 것을 방지
                        stopCountDown()
                    } else {
                        val seekBarMinutes: Int = seekBar!!.progress
                        val initialMills: Long = seekBarMinutes * 60 * 1000L
                        startCountDown(initialMills)
                    }
                }
            }
        )
    }

    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell,1)
    }

    private fun startCountDown(initialMills: Long) {
        currentCountDownTimer = createCountDownTimer(initialMills)
        currentCountDownTimer?.start()

        tickingSoundId?.let {   tickingSoundId ->
            soundPool.play(tickingSoundId, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    //See: https://developer.android.com/reference/android/os/CountDownTimer
    private fun createCountDownTimer(initialMills: Long): CountDownTimer {
        return object : CountDownTimer(initialMills, 1000L) {
            override fun onTick(millisUntilFinished: Long) {//countDownInterval 마다 onTick 호출 -> UI 세팅
                updateUIbyRemainTime(millisUntilFinished)
                updateSeekBarUI(millisUntilFinished)
            }
            override fun onFinish() {
                finishCountDownTimer()
            }
        }
    }

    private fun finishCountDownTimer() {
        updateUIbyRemainTime(0)
        updateSeekBarUI(0)

        soundPool.autoPause()
        bellSoundId?.let { bellSoundId ->
            soundPool.play(bellSoundId, 1F, 1F, 0, 0, 1F)
        }
    }

    private fun updateUIbyRemainTime(remainMills: Long) {
        val remainSeconds = remainMills / 1000
        binding.remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        binding.remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBarUI(remainMills: Long){
        val remainMinutes: Int = (remainMills / 1000 / 60).toInt()
        binding.seekBar.progress =  remainMinutes
    }
}