
1. webView

2. swipeRefreshLayout

3. contentLoadingProgressBar


editText
android:selectAllOnFocus="true"


        <!-- Customize your theme here. -->
        <item name="android:windowLightStatusBar">true</item>


webView
기본적으로 URl 주소를 입력하면 디폴트 브라우저가 실행되는데
오버라이딩을 통해서 커스텀된 브라우저에서 실행시킬 수

`binding.webView.webViewClient = WebViewClient()`

보안상의 이유로 안드로이드에서는 자바스크립트 코드를 차단하고 있는데
이로 인해 웹페이지 사용에 제한이 있음 -> 설정을 바꿔서 자바스크립트 코트를 허용함

`binding.webView.settings.javaScriptEnabled = true`


    private fun bindViews(){
        binding.addressBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { //주소를 입력하고 주소창을 닫은 경우
                binding.webView.loadUrl(textView.text.toString())
            }
            return@setOnEditorActionListener false
        /*  리턴값이 트루라면 이벤트를 소비했다는 뜻으로 다른곳에서는 데이터를핸들링할필요없음
            actionDone에서는 이 데이터가 필요하기 때문에 false
             */
        }
    }

                        android:layout_width="0dp"
                        android:layout_height="0dp"
            app:layout_constraintDimensionRatio="1:1" 컨스트레인트 레이아웃 하위 속성은 ratio 속성을 줄수있음


           android:background="?attr/selectableItemBackground"  //??

android:layout_height="?attr/actionBarSize" 현재프로젝트의 테마속성에 접근가능

///////////////////

Swiperefreshlayout
`implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"`


```xml
//1. xml 에서 새로고칠 영역을 swiperefreshlayout 으로 감싼다
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/refreshLayout">
    <WebView
        android:id="@+id/webView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

//2. 기능 구현
binding.refreshLayout.setOnRefreshListener {
    binding.webView.reload()
}

    inner class WebViewClient: android.webkit.WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            binding.refreshLayout.isRefreshing = false
        }
    }
```
////////////////////
3. progressBar

        <androidx.core.widget.ContentLoadingProgressBar
            android:id="@+id/progressBar"
            style="@style/Widget.AppCompat.ProgressBar.Horizontal"
            android:layout_width="0dp"
            android:layout_height="2dp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/toolbar" />



WebViewClient
컨텐츠 로딩 이벤트 처리


WebChromClient
브라우저 관점의 이벤트 오버라이드 해서 사용

/////////////////


Manifest
android:usesCleartextTraffic="true"
http 로 접근하면 보안이슈가 있지만 보안을 크게 신경쓰지 않은 앱이기 때문에 옵션을 주어서 해결

->editText 관련
EditText의 인풋 타입에 따라서 시스템이 알맞은 키보드를 띄워줌
https://developer.android.com/training/keyboard-input/style

imeOptions 설명
https://developer.android.com/reference/android/widget/TextView#attr_android:imeOptions

인풋 타입 종류
https://developer.android.com/reference/android/widget/TextView#attr_android:inputType

웹뷰 디폴트 브라우저 설명
https://developer.android.com/guide/webapps/webview

swipeRefreshLayout
https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout

progressBar
https://developer.android.com/reference/android/widget/ProgressBar.html

contentLoadingProgressBar
https://developer.android.com/reference/androidx/core/widget/ContentLoadingProgressBar

