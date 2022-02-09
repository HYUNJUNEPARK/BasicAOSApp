# 5_PomodoroTimer

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/5_PomodoroTimer.png" height="400"/>

---

>**SeekBar**</br>
```xml
<!--xml-->
<SeekBar android:id="@+id/seekBar"
    android:max="60"
    android:progressDrawable="@drawable/seekbar_progress"
    android:thumb="@drawable/seekbar_thumb" />
```

```kotlin
binding.seekBar.setOnSeekBarChangeListener(
    object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //Notification that the user has started a touch gesture
        }
        override fun onProgressChanged(seekBar: SeekBar?, progressMinute: Int, fromUser: Boolean) {
            //Notification that the progress level has changed
        }
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //Notification that the user has finished a touch gesture
        }
    }
)
```

>**SoundPool**</br>
```kotlin
private var tickingSoundId: Int? = null

//1. Load
private fun initSounds() {
    tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
}
//2. Play
private fun startCountDown(initialMills: Long) {
    tickingSoundId?.let {   tickingSoundId ->
        soundPool.play(tickingSoundId, 1F, 1F, 0, -1, 1F)
    }
}
//3. Pause
private fun stopCountDown() {
    soundPool.autoPause()
}
//4. Release *사운드 파일이 메모리에 계속 올라가 있으면 자원낭비
override fun onDestroy() {
    super.onDestroy()
    soundPool.release()
}
```

>**CountDownTimer**</br>
```kotlin
private var currentCountDownTimer: CountDownTimer? = null

private fun createCountDownTimer(initialMills: Long): CountDownTimer {
    return object : CountDownTimer(initialMills/*long millisInFuture*/, 1000L/*long countDownInterval*/) {
        override fun onTick(millisUntilFinished: Long) {
            //long countDownInterval 마다 onTick 호출 -> UI 세팅
        }
        override fun onFinish() {
            //Callback fired when the time is up
        }
    }
}

//1. Start
//터치를 떼면 onStopTrackingTouch() 에서 seekBar 의 progress 값이 ms 로 바뀌어 전달됨
private fun startCountDown(initialMills: Long) {
    currentCountDownTimer = createCountDownTimer(initialMills)
    currentCountDownTimer?.start()
}

private fun initSeekBar() {
    binding.seekBar.setOnSeekBarChangeListener(
        object : SeekBar.OnSeekBarChangeListener {
            /*
            ...
            */
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar == null) return

                if (seekBar.progress == 0) {
                    //0에서 바로 떼면 0인 상태에서 타이머가 실행되는 것을 방지
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

//2. Cancel
private fun stopCountDown() {
    currentCountDownTimer?.cancel()
    currentCountDownTimer = null
}
```

>**참고링크**</br>

시크바 커스텀</br>
https://ghj1001020.tistory.com/19

OnSeekBarChangeListener</br>
https://developer.android.com/reference/android/widget/SeekBar.OnSeekBarChangeListener

SoundPool</br>
https://developer.android.com/reference/android/media/SoundPool

CountDownTimer</br>
https://developer.android.com/reference/android/os/CountDownTimer