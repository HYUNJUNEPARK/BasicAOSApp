# BMICalculator

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/1_BMICalculator.jpg" height="400"/>

---
1. Dialog</br>
2. string.xml 에서 Toast 메시지 갖고오기</br>
3. findViewById<>()사용</br>
4. string.xml 의 String 데이터 colorString 으로 사용하는 법</br>
---
>**1. Dialog**</br>
```kotlin
//1. View/Builder 세팅
val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
val mDialogBuilder = AlertDialog.Builder(this).setView(mDialogView)

//2. Dialog 오픈
val mAlertDialog = mDialogBuilder.show()

//3. Dialog 닫기
mAlertDialog.dismiss()
```
<br></br>
<br></br>
>**2. string.xml 에서 Toast 메시지 갖고오기**</br>

`Toast.makeText(this, R.string.toast, Toast.LENGTH_SHORT).show()`
<br></br>
<br></br>

>**3. findViewById<>()사용**</br>

`val bmiValueTextView = mDialogView.findViewById<TextView>(R.id.tv_bmi_value)`
<br></br>
<br></br>
>**4. string.xml 의 String 데이터 colorString 으로 사용하는 법** - public static int parseColor(String colorString)</br>
```kotlin
when (resultText) {
    getString(R.string.obese) -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.red)}"))
    //...
    else -> bmiResult.setTextColor(Color.parseColor("#${getString(R.string.black)}"))
}
```
<br></br>
<br></br>

---


>**참고링크**</br>

**소스코드에서 XML 텍스트 컬러 속성 변경**</br>
https://hashcode.co.kr/questions/344/%EC%BD%94%EB%93%9C%EB%A1%9C-textview%EC%9D%98-%EA%B8%80%EC%9E%90%EC%83%89%EC%9D%84-%EB%B0%94%EA%BF%80%EC%88%98-%EC%9E%88%EB%82%98%EC%9A%94

**Custom Dialog**</br>
https://philosopher-chan.tistory.com/1015
