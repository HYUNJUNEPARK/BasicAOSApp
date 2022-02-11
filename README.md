# SecretDiary

4. SharedPreference</br>
앱 설정 값 등 내부 저장소에 간단한 데이터를 저장할 수 있음</br>
데이터를 Key:Value 로 저장하며 종료되어도 데이터가 남아있음</br>
SharedPreferences 생성</br>
- getSharedPreferences("name", MODE_)</br>
데이터 넣기</br>
- sharedPreferences.putString("name", data)</br>
데이터 꺼내기</br>
- sharedPreferences.getString("name", "defaultData")</br>
</br>




<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/3_SecretDiary.png" height="400"/>

---
1. <a href = "#content1">Font custom</a></br>
2. <a href = "#content2">AlertDialog</a></br>
3. <a href = "#content3">Thread</a></br>
4. <a href = "#content4">SharedPreference</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**Font custom**</a></br>



res 우클릭  -> New -> Android Rescouce Directory -> font 디렉토리 생성(res 는 대문자를 사용하지 않으므로 소문자로만 파일이름 설정)

'android:fontFamily="@font/폰트 이름" //xml'


<br></br>
<br></br>

><a id = "content2">**AlertDialog**</a></br>
```kotlin
//코드 샘플
```
<br></br>
<br></br>

><a id = "content3">**Thread**</a></br>
```kotlin
//코드샘플
```
<br></br>
<br></br>

><a id = "content4">**SharedPreference**</a></br>



```kotlin
//1. onCreate 영역 내부에서 SharedPreferences 생성
val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

//2. 쓰기


//3. 읽기



```
<br></br>
<br></br>

---
><a id = "ref">**참고링크**</a></br>


액션바 뒤로가기 버튼</br>
https://programmingworld1.tistory.com/15</br>

SharedPreference 설명</br>
http://zeany.net/24</br>

