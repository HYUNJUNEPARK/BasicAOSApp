# SecretDiary
비밀번호 설정이 가능한 메모장 앱

![cover](https://user-images.githubusercontent.com/89306567/148688482-2e12c484-e1e7-422d-bb7b-48e018d8f8ec.png)

</br>
1. 사용자 폰트 사용</br>
res 우클릭  -> New -> Android Rescouce Directory -> font 디렉토리 생성</br>
res는 대문자를 사용하지 않으므로 소문자로만 파일이름 설정</br>
xml 파일 android:fontFamily="@font/폰트 이름"</br>
</br>
2. AlertDialog
</br></br>
3.Thread
</br></br>
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
**참고링크** </br>
액션바 뒤로가기 버튼 참고 블로그</br>
https://programmingworld1.tistory.com/15
