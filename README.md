

//새로 배운 기능
1. contraintLayout 속성\n
<Button\n
...\n
   app:layout_constraintHorizontal_chainStyle="packed"\n
...\n
/>\n

2. shape drawable\n
-drawable 우클릭 new -> Drawable Resource File\n
-XML 파일로 쉽게 이미지를 생성할 수 있음\n
-안드로이드 앱 내부에 있는 XML 파일로 불러오려면 context가 필요함\n
textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)\n

3. NumberPicker\n
-minValue, maxValue 로 범위 지정\n
binding.numberPicker.minValue = 1\n
binding.numberPicker.maxValue = 45\n

4. forEachIndexed\n
- 객체뿐 아니라 인덱스 데이터도 필요할 때 사용\n
 numberList.forEachIndexed { index, number -> \n
   ...\n
}\n

