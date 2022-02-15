# 6_Recorder

<img src="이미지 주소" height="400"/>

---
1. <a href = "#content1">CustomView</a></br>
2. <a href = "#content2">MediaRecorder</a></br>
3. <a href = "#content3">State Variable</a></br>
4. <a href = "#content4">Scoped Storage</a></br>
4. <a href = "#content5">Custom Drawing</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. CustomView**</a></br>


state 에 따라서 UI가 바뀌는 버튼 구현

**AppCompat**</br>
매년 새로운 버전의 안드로이드가 출시되는데 이전 버전의 호환성이 필요함</br>
AppCompat 은 기존 버전을 래핑해서 새로운 기능 중 대부분의 것들을 정상적으로 동작하게 지원해주는 라이브러리</br>

1. 서브클래스 만들기 `class PieChart(context: Context, attrs: AttributeSet) : View(context, attrs)`</br>

```kotlin
//state 값에 따라서 UI 가 바뀌는 ImageButton 클래스
class RecordButton(context: Context, attrs: AttributeSet) : AppCompatImageButton(context, attrs) {
    fun updateIconWithState(state: State){
        when(state) {
            State.BEFORE_RECORDING -> setImageResource(R.drawable.ic_record)
            //...
        }
    }
}

//음성을 시각화해주는 SoundVisualizerView 클래스
class SoundVisualizerView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    //...
}
```

2. XML 에 배치</br>
```xml
<com.june.recorder.RecordButton
android:id="@+id/recordButton"
    //...
/>
```

3. 소스코드에서 사용</br>
```kotlin
private fun initViews() {
    binding.recordButton.updateIconWithState(state)
}
```
<br></br>
<br></br>

><a id = "content2">**2. MediaRecorder**</a></br>

안드로이드가 기본적으로 제공하는 Recording API 에는 Audio Recorder 와 Media Recorder 가 있음</br>
Audio Recorder 의 경우 오디오만 녹음 가능하지만, Media Recorder 는 오디오 및 비디오 녹음이 가능</br>

1.MediaRecorder 와 MediaPlayer 변수 준비</br>
`private var player: MediaPlayer? = null`

`private var recorder: MediaRecorder? = null`

2. 녹음기 세팅과 녹음 시작</br>
```kotlin
private fun startRecording() {
    //녹음기 세팅
    recorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC) //오디오 입력 형식
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) //오디오 출력 형식
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) //인코딩 형식
        setOutputFile(recordingFilePath) //4.Scoped Storage 정리 내용 참고
        prepare()
    }
    //녹음 시작
    recorder?.start() // Recording is now started
}
```

3. 녹음 정지</br>
```kotlin
private fun stopRecording() {
    recorder?.run {
        stop()
        release() //메모리 해제. obj 는 재사용 될 수 없음
    }
    recorder = null
}
```

4. 플레이어 세팅과 녹음파일 재생</br>
```kotlin
private fun startPlaying() {
    //플레이어 세팅
    player = MediaPlayer().apply {
        setDataSource(recordingFilePath) //4.Scoped Storage 정리 내용 참고
        prepare()
    }
    //녹음파일 재생
    player?.start()
}
```

5. 재생 정지</br>
```kotlin
    private fun stopPlaying() {
        player?.release()
        player = null
    }
```
<br></br>
<br></br>

><a id = "content3">**3. State Variable**</a></br>

state 관리 변수로 state 의 값에 따라 Button UI 가 바뀜

```kotlin
private var state = State.BEFORE_RECORDING
    set(value) { //state 가 변할 때마다 호출됨
        field = value //field == private var state
        binding.recordButton.updateIconWithState(value)
        binding.resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)

/*바로 위 코드와 같은 내용
if((value == State.AFTER_RECORDING) || (value ==State.ON_PLAYING)){
    binding.resetButton.isEnabled = true
}
else {
    binding.resetButton.isEnabled = false
}*/
```
<br></br>
<br></br>

><a id = "content4">**4. Scoped Storage**</a></br>

**내부 저장소**</br>
앱 설치시 앱 마다 갖고 있는 공간</br>
cache, files 폴더가 자동으로 생성되며 READ/WRITE 에 대한 권한이 필요 없으며 앱 삭제 시 함께 삭제됨

`${cacheDir.absolutePath}`</br>
/data/user/0/패키지명/cache</br>
 `${filesDir.absolutePath}`</br>
/data/user/0/패키지명/files</br>


**외부 저장소**</br>
a. App 전용 공간(내부 저장소와 비슷)</br>
앱마다 갖고 있는 고유의 공간으로 READ/WRITE 기능을 하며 앱 삭제 시 함께 제거됨</br>
다른 앱은 자신의 데이터 폴더에 접근할 수 없음</br>
FileProvider 를 사용해 Uri 를 넘기는 게 가능함

 `${externalCacheDir?.absolutePath}` /storage/emulated/0/Android/data/패키지명/cache</br>
`${getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath}` /storage/emulated/0/Android/data/패키지명/files/Pictures</br>


b. 공용 공간</br>
다른 앱들과 데이터 공유가 이루어지는 공간으로 앱 삭제 여부와 상관없이 계속 남아 있음</br>
READ/WRITE 시 사용자 동의를 받아야함</br>
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

`${Environment.getExternalStorageDirectory()}` /storage/emulated/0</br>
`${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)` /storage/emulated/0/Pictures</br>
<br></br>
<br></br>


><a id = "content5">**5. Custom Drawing**</a></br>

**Paint**</br>
그리는 도형의 색상, 스타일, 글꼴 등을 정의</br>
그림을 그리기 전 하나 이상의 Paint 객체를 만들어야함</br>
onDraw() 메서드 내에서 Paint 객체를 만들면 성능 저하됨</br>

```kotlin
//Paint 객체
private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = context.getColor(R.color.purple_500)
    strokeWidth = LINE_WIDTH
    strokeCap = Paint.Cap.ROUND
}
```

**Canvas**</br>
화면에 그릴 수 있는 도형을 정의</br>
onDraw(canvas: Canvas?) 를 오버라이딩해 맞춤 사용자 UI 를 만듬</br>

```kotlin
override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    //...
    canvas.drawLine(startX, startY, stopX, stopY, amplitudePaint)
}
```
<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

저장소의 종류(Scoped Storage)</br>
https://black-jin0427.tistory.com/236</br>
https://codechacha.com/ko/android-data-storage/</br>

음성 녹음을 위한 MediaRecorder</br>
https://codetravel.tistory.com/11</br>

Custom Drawing</br>
https://developer.android.com/training/custom-views/custom-drawing</br>

View 의 각종 활성화 비활성화 상태</br>
https://arabiannight.tistory.com/352</br>

CreateView</br>
https://developer.android.com/training/custom-views/create-view</br>

MediaRecorder</br>
https://developer.android.com/guide/topics/media/mediarecorder</br>

MediaPlayer</br>
https://developer.android.com/guide/topics/media/mediaplayer</br>

오디오 지원형식
https://developer.android.com/guide/topics/media/media-formats</br>

권한 요청</br>
https://developer.android.com/training/permissions/requesting</br>


