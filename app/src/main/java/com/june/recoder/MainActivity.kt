package com.june.recoder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june.recoder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var state = State.BEFORE_RECORDING
        set(value) { //state 가 변할 때 호출됨
            field = value
            binding.recordButton.updateIconWithState(value)
        }
    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private var recorder: MediaRecorder? = null
    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }
    private var player: MediaPlayer? = null

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        requestAudioPermission()
        bindViews()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted: Boolean =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            finish()
        }
    }

    private fun initViews() {
        binding.recordButton.updateIconWithState(state)
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun bindViews() {
        binding.recordButton.setOnClickListener {
            when(state){
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }
    }

    //See: https://developer.android.com/reference/android/media/MediaRecorder
    //https://developer.android.com/guide/topics/media/media-formats
    private fun startRecording() {
        recorder = MediaRecorder()
            .apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(recordingFilePath)
                prepare();
            }
        recorder?.start()
        state = State.ON_RECORDING
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release() //메모리 해제
        }
        recorder = null
        state = State.AFTER_RECORDING
    }

    //See: https://developer.android.com/reference/android/media/MediaPlayer
    private fun startPlaying() {
        player = MediaPlayer()
            .apply {
                setDataSource(recordingFilePath)
                prepare()
            }
        player?.start()
        state = State.ON_PLAYING
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        state = State.AFTER_RECORDING
    }
}