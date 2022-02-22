# SecretDiary

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/basicApp/3_SecretDiary.png" height="400"/>

---
1. <a href = "#content1">Font custom</a></br>
2. <a href = "#content2">AlertDialog</a></br>
3. <a href = "#content3">Thread</a></br>
4. <a href = "#content4">SharedPreference</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. Font custom**</a></br>

res 우클릭  -> New -> Android Resource Directory -> font 디렉토리 생성(res 는 대문자를 사용하지 않으므로 소문자로만 파일이름 설정)

`android:fontFamily="@font/폰트 이름" //xml`
<br></br>
<br></br>

><a id = "content2">**2. AlertDialog**</a></br>
```kotlin
AlertDialog.Builder(this)
    .setTitle(R.string.error)
    .setMessage(R.string.wrong_pw)
    .setPositiveButton(R.string.confirm) { _, _ ->
        initializeNumberPicker()
    }
    .create()
    .show()
}
```
<br></br>
<br></br>

><a id = "content3">**3. Thread**</a></br>
**백그라운드 스레드(서브 스레드)**

-하나의 앱에 여러개 존재 가능

-원칙적으로 백그라운드 스레드는 UI 구성요소에 접근하면 안됨. 단, UI에 접근할 때는 runOnUiThread 블럭으로 감싸줘야 함

**Message Queue**

-다른 스레드 또는 자신으로 부터 전달 받은 메시지를 보관하는 Queue

**핸들러**

-루퍼로 부터 받은 메시지 또는 Runnable 객체를 처리 or 메시지를 받아 Message Queue 에 넣는 스레드간 통신 장치

-루퍼는 앱이 실행되면 자동으로 하나 생성되어 무한루프를 돌지만 핸들러는 개발자가 직접 작성해야함

```kotlin
//메인 스레드에 연결된 핸들러
private val handler = Handler(Looper.getMainLooper())
```

```kotlin
//핸들러가 루퍼로 부터 받은 Runnable 객체 처리
binding.diaryEditText.addTextChangedListener {
    handler.removeCallbacks(saveRunnable) //postDelayed() 가 실행되기 전에 runnable 이 있다면 지움
    handler.postDelayed(saveRunnable, 500) //500ms 에 한번씩 runnable 호출
}
```

**루퍼(Looper)**

-메인액티비티가 실행됨과 동시에 for 문 하나가 무한루프를 돌고 있는 서브 스레드

-대기하고 있다가 자신의 큐(Message Queue)에 쌓인 메시지와 Runnable 객체를 차례로 핸들러에 전달

<br></br>
<br></br>

><a id = "content4">**4. SharedPreference**</a></br>

디바이스 내부 저장소에 앱 설정 값 등 간단한 데이터를 저장할 수 있음

데이터를 Key:Value 로 저장하며 종료되어도 데이터가 남아있음

```kotlin
/*1. SharedPreferences 생성 (onCreate 영역 내부에서)
설정파일을 로드 시 모드
a. MODE_PRIVATE : 자신의 app 내에서 사용할때 기본값
b. MODE_WORLD_READABLE : 다른 app 에서 읽기 가능
c. MODE_WORLD_WRITEABLE : 다른 app 에서 쓰기 가능*/
val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

//2. 쓰기 - commit
//방법 1.
passwordPreferences.edit(true) { putString("password", input) }
//방법 2.
passwordPreferences.edit() {
    putString("password", input)
    commit()
}

//3. 읽기
val password = passwordPreferences.getString("password", "default value") //String getString(String var1, @Nullable String var2);

```

**edit**</br>
-람다로 SharedPreferences 값 수정 가능</br>

**commit()** //public abstract boolean commit ()</br>
-동기적 저장 (파일이 다 저장될때까지 UI를 멈추고 기다림) - 가벼운 작업에서 사용</br>
-저장 성공 시 true 반환</br>
-edit 는 기본적으로 commit 이 false</br>

**apply()** //public abstract void apply ()</br>
-비동기적 저장</br>
-반환값 없음</br>
<br></br>
<br></br>

---
><a id = "ref">**참고링크**</a></br>

액션바 뒤로가기 버튼</br>
https://programmingworld1.tistory.com/15</br>

SharedPreference 사용법</br>
http://zeany.net/24</br>

SharedPreferences 에서 commit 과 apply 의 차이</br>
http://sukjinni.blogspot.com/2015/09/sharedpreferences-commit-apply.html</br>


