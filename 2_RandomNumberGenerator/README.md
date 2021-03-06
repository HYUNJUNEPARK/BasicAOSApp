# RandomNumberGenerator

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/basicApp/2_RandomNumberGenerator.jpg" height="400"/>

---
1. <a href = "#content1">Shape Drawable</a></br>
2. <a href = "#content2">NumberPicker</a></br>
3. <a href = "#content3">forEachIndexed</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. Shape Drawable**</a></br>

XML 로 쉽게 Drawable 객체를 생성하며 APK 용량을 줄여주고 쉽게 모양을 바꿀 수 있음

배경이미지를 만들 때 사용

drawable 우클릭 new -> Drawable Resource File -> Root Element 에 shape 입력

```xml
<shape android:shape="[rectangle|oval|line|ring]"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/orange"/>
    <size
        android:width="15dp"
        android:height="15dp"/>
</shape>
```

소스코드에서 Shape Drawable 을 불러와 사용하려면 ContextCompat.getDrawable()에 context 가 필요

`textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)`

TextView 에 Shape drawable 사용

```xml
<TextView
    android:id="@+id/ball1"
    android:background="@drawable/circle_blue"/>
```

<br></br>
<br></br>
><a id = "content2">**2. NumberPicker**</a></br>

minValue, maxValue 로 범위 지정해 사용

```kotlin
binding.numberPicker.minValue = 1
binding.numberPicker.maxValue = 45
```
<br></br>
<br></br>
><a id = "content3">**3. forEachIndexed**</a></br>

리스트에서 객체 데이터와 인덱스 데이터가 필요할 때 사용

```kotlin
val numberList:List<Int> = makeRandomNumber()

numberList.forEachIndexed { indexData, objData ->
//...    
}
```
---
><a id = "ref">**참고링크**</a></br>

Shape Drawable</br>
https://overoid.tistory.com/30</br>
