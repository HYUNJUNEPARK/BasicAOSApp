![1_Screenshot_20211231-231107_RandomNumberGenerator](https://user-images.githubusercontent.com/89306567/147827749-dab3791e-7b92-4c26-9e67-265ea4cd1e65.jpg)
랜덤 번호 6 개를 생성해주는 앱 <br />
번호 지정 버튼으로 6가지 랜덤 번호 중 최대 5가지 번호를 지정할 수 있으며 번호 생성 버튼으로 랜덤 번호를 생성 

//새로 배운 기능
1. contraintLayout 속성<br />
<Button <br />
... <br />
   app:layout_constraintHorizontal_chainStyle="packed" <br />
... <br />
/> <br />

2. shape drawable<br />
-drawable 우클릭 new -> Drawable Resource File<br />
-XML 파일로 쉽게 이미지를 생성할 수 있음<br />
-안드로이드 앱 내부에 있는 XML 파일로 불러오려면 context가 필요함<br />
textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)<br />

3. NumberPicker<br />
-minValue, maxValue 로 범위 지정<br />
binding.numberPicker.minValue = 1<br />
binding.numberPicker.maxValue = 45<br />

4. forEachIndexed<br />
- 객체뿐 아니라 인덱스 데이터도 필요할 때 사용<br />
 numberList.forEachIndexed { index, number -> <br />
   ..<br />
}<br />

