

//새로 배운 기능
1. contraintLayout 속성
<Button
...
   app:layout_constraintHorizontal_chainStyle="packed"
...
/>

2. shape drawable
-drawable 우클릭 new -> Drawable Resource File
-XML 파일로 쉽게 이미지를 생성할 수 있음
-안드로이드 앱 내부에 있는 XML 파일로 불러오려면 context가 필요함
textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)

3. NumberPicker
-minValue, maxValue 로 범위 지정
binding.numberPicker.minValue = 1
binding.numberPicker.maxValue = 45

4. forEachIndexed
- 객체뿐 아니라 인덱스 데이터도 필요할 때 사용
 numberList.forEachIndexed { index, number -> 
   ...
}

