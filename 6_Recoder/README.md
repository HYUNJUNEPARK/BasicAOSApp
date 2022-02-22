# 6_Recorder

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/6_Recoder.png" height="400"/>

---
1. <a href = "#content1">CustomView</a></br>
2. <a href = "#content2">State Variable</a></br>
3. <a href = "#content3">MediaRecorder</a></br>
4. <a href = "#content4">Scoped Storage</a></br>
5. <a href = "#content5">Custom Drawing</a></br>
6. <a href = "#content6">handler</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. CustomView**</a></br>



-XML 에서 뷰 컴포넌트를 사용할 수 있고, 소스코드에서 CustomView 클래스 내부에 있는 메서드도 사용할 수 있음</br>
-기존 뷰를 상속받았지만 내부에 메서드를 갖고 있어 사용자가 기능을 커스텀해서 사용 ex)state 에 따라서 UI가 바뀌는 버튼 구현</br>


(1) 서브클래스 만들기 `class PieChart(context: Context, attrs: AttributeSet) : View(context, attrs)`</br>

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

(2) XML 에 배치</br>
```xml
<com.june.recorder.RecordButton
android:id="@+id/recordButton"
    //...
/>
```

(3) 소스코드에서 사용</br>
```kotlin
private fun initViews() {
    binding.recordButton.updateIconWithState(state)
}
```

**AppCompat**</br>
-매년 새로운 버전의 안드로이드가 출시되는데 이전 버전의 호환성이 필요함</br>
-AppCompat 은 기존 버전을 래핑해서 새로운 기능 중 대부분의 것들을 정상적으로 동작하게 지원해주는 라이브러리</br>
<br></br>
<br></br>


><a id = "content2">**2. State Variable**</a></br>

-state 관리 변수로 state 의 값에 따라 Button UI 가 바뀜</br>

```kotlin
private var state = State.BEFORE_RECORDING
    set(value) { //state 가 변할 때마다 호출됨
        field = value //field == private var state
        binding.recordButton.updateIconWithState(value)
        binding.resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)
    }

/*binding.resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING) 같은 코드
>>
if((value == State.AFTER_RECORDING) || (value ==State.ON_PLAYING)){
    binding.resetButton.isEnabled = true
}
else {
    binding.resetButton.isEnabled = false
}*/
```
<br></br>
<br></br>



><a id = "content3">**3. MediaRecorder**</a></br>

-안드로이드가 기본적으로 제공하는 Recording API 에는 Audio Recorder 와 Media Recorder 가 있음</br>
-Audio Recorder 의 경우 오디오만 녹음 가능하지만, Media Recorder 는 오디오 및 비디오 녹음이 가능</br>

(1) MediaRecorder 와 MediaPlayer 변수 준비</br>
`private var player: MediaPlayer? = null`
`private var recorder: MediaRecorder? = null`

(2) 녹음기 세팅과 녹음 시작</br>
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

(3) 녹음 정지</br>
```kotlin
private fun stopRecording() {
    recorder?.run {
        stop()
        release() //메모리 해제. obj 는 재사용 될 수 없음
    }
    recorder = null
}
```

(4) 플레이어 세팅과 녹음파일 재생</br>
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

(5) 재생 정지</br>
```kotlin
    private fun stopPlaying() {
        player?.release()
        player = null
    }
```
<br></br>
<br></br>



><a id = "content4">**4. Scoped Storage**</a></br>

**내부 저장소**</br>
-앱 설치시 앱 마다 갖고 있는 공간</br>
-cache, files 폴더가 자동으로 생성되며 READ/WRITE 에 대한 권한이 필요 없으며 앱 삭제 시 함께 삭제됨

`${cacheDir.absolutePath}` /data/user/0/패키지명/cache</br>
`${filesDir.absolutePath}` /data/user/0/패키지명/files</br>

<br></br>
**외부 저장소**</br>
a. App 전용 공간(내부 저장소와 비슷)</br>
-앱마다 갖고 있는 고유의 공간으로 READ/WRITE 기능을 하며 앱 삭제 시 함께 제거됨</br>
-다른 앱은 자신의 데이터 폴더에 접근할 수 없음</br>
-FileProvider 를 사용해 Uri 를 넘기는 게 가능함

`${externalCacheDir?.absolutePath}` /storage/emulated/0/Android/data/패키지명/cache</br>
`${getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath}` /storage/emulated/0/Android/data/패키지명/files/Pictures</br>


b. 공용 공간</br>
-다른 앱들과 데이터 공유가 이루어지는 공간으로 앱 삭제 여부와 상관없이 계속 남아 있음</br>
-READ/WRITE 시 사용자 동의를 받아야함</br>

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
-그리는 도형의 색상, 스타일, 글꼴 등을 정의</br>
-그림을 그리기 전 하나 이상의 Paint 객체를 만들어야함</br>
-onDraw() 메서드 내에서 Paint 객체를 만들면 성능 저하됨</br>

```kotlin
private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = context.getColor(R.color.purple_500)
    strokeWidth = LINE_WIDTH
    strokeCap = Paint.Cap.ROUND
}
```
<br></br>
**Canvas**</br>
-화면에 그릴 수 있는 도형을 정의</br>
-onDraw(canvas: Canvas?) 를 오버라이딩해 맞춤 사용자 UI 를 만듬</br>

```kotlin
override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    //...
    canvas.drawLine(startX, startY, stopX, stopY, amplitudePaint)
}
```
<br></br>
<br></br>

><a id = "content6">**6. handler**</a></br>

**Runnable 인터페이스**</br>

Runnable 인터페이스 기본 구현 예시</br>
```kotlin

class WorkerRunnable: Runnable {
    override fun run() {
        //...
    }
}

//onCreate()
var thread = Thread(WorkerRunnable(())
thread.start
```
<br></br>
**SoundVisualizerView 과정**</br>
a. 핸들러가 Runnable 객체를 메세지 큐에 전달
```kotlin
fun startVisualizing(isReplaying: Boolean) {
    this.isReplaying = isReplaying
    handler?.post(visualizeRepeatAction) //1.
}
```
<br></br>
b-1. 빈 파라미터를 전달하고 Int 를 반환 받는 var onRequestCurrentAmplitude 초기화</br>
```kotlin
//SoundVisualizerView.kt
var onRequestCurrentAmplitude: (() -> Int)? = null //3-3.
```
-현재 진폭을 요청해 반환된 값을 받는 변수</br>
-변수지만 함수 바디를 갖고 있으며 함수 바디는 MainActivity 에 구현돼 있음

b-2. var onRequestCurrentAmplitude 함수 부분</br>
```kotlin
//MainActivity.kt
binding.soundVisualizerView.onRequestCurrentAmplitude = {
    recorder?.maxAmplitude ?:0 //3-2
}
```
-빈 파라미터()를 전달받고 maxAmplitude 를 반환

<br></br>
c. 20ms 마다 처리되는 Runnable 객체</br>
```kotlin
private val visualizeRepeatAction: Runnable = object : Runnable  {
    override fun run() {
        if (!isReplaying) {
            val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0 //3-1 & 3-3
            drawingAmplitudesList = listOf(currentAmplitude) + drawingAmplitudesList
        }
        else {
            replayingPosition++
        }
            invalidate() //4.
            handler?.postDelayed(/*Runnable Obj*/this, ACTION_INTERVAL) //2.
    }
}

```
<br></br>
(1) 핸들러가 Runnable 객체를 메세지 큐에 전달
`handler?.post(visualizeRepeatAction)`


(2) 핸들러가 20ms 뒤 Runnable 객체 이벤트 발생</br>
```kotlin
handler?.postDelayed(/*Runnable Obj*/this, ACTION_INTERVAL)
//public final boolean postDelayed(@NonNull Runnable r, long delayMillis)
```
-핸들러 생성 시점의 스레드(보통 메인스레드)의 메시지 큐에 있는 Runnable 객체를 전달받아 처리</br>
-설정한 딜레이(long delayMillis)가 지난 후 작업이 수행 됨</br>


(3-1) invoke() 가 var onRequestCurrentAmplitude 를 호출</br>
`val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0`

(3-2) MainActivity 에서 binding.soundVisualizerView.onRequestCurrentAmplitude 가 실행되면서 maxAmplitude 를 반환
```kotlin
binding.soundVisualizerView.onRequestCurrentAmplitude = {
    recorder?.maxAmplitude ?:0
}
```

(3-3) val currentAmplitude 가 maxAmplitude 를 반환받음</br>
`var onRequestCurrentAmplitude: (() -> Int)? = null`</br>
`val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0`</br>

(4) 뷰 다시그리기</br>
`invalidate()`


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

오디오 지원형식</br>
https://developer.android.com/guide/topics/media/media-formats</br>

권한 요청</br>
https://developer.android.com/training/permissions/requesting</br>


