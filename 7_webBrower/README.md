# WebBrowser

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/basicApp/7_WebBrowser.png" height="400"/>

---
1. <a href = "#content1">webView</a></br>
2. <a href = "#content2">swipeRefreshLayout</a></br>
3. <a href = "#content3">contentLoadingProgressBar</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. webView**</a></br>


-URl 주소를 입력하면 디폴트 브라우저앱이 실행되는데 오버라이딩을 통해서 커스텀된 브라우저앱을 사용할 수 있음</br>

```xml
//AndroidManifest.xml
//http 로 접근하면 보안 이슈가 있어 앱 실행에 문제가 생김.
//해당 앱은 기능 구현에 중점을 둔 앱이기 때문에 http 프로토콜 사용 허용
android:usesCleartextTraffic="true"
```

```kotlin
//webView 세팅
binding.webView.apply {
    webViewClient = WebViewClient()
    webChromeClient = WebChromeClient()
    settings.javaScriptEnabled = true //1.javaScriptEnabled
    loadUrl(DEFAULT_URL)
}

//2. WebViewClient
inner class WebViewClient: android.webkit.WebViewClient() {
//class 앞에 inner 를 붙여야 상위 클래스(MainActivity)에 있는 속성에 접근할 수 있음
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        //...
    }

    override fun onPageFinished(view: WebView?, /*redirecting url*/url: String?) {
        super.onPageFinished(view, url)
        //...
    }
}

//3. WebChromeClient
inner class WebChromeClient() : android.webkit.WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        //...
    }
}

//4. 사용자 주소로 이동
private fun bindViews(){
    binding.addressBar.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { ////키보드 엔터 버튼 클릭 시
                val loadingUrl = textView.text.toString()
                if (URLUtil.isNetworkUrl(loadingUrl)) { //addressBar 에 입력된 값이 URL 형식이라면
                    binding.webView.loadUrl(loadingUrl) //URL 로드
            }
            else {
                binding.webView.loadUrl("http://$loadingUrl")
            }
        }
        return@setOnEditorActionListener false
    }
    //..
}


```
(1) `settings.javaScriptEnabled = true`</br>
-보안상의 이유로 AOS 에서는 자바스크립트 코드를 차단하고 있는데 이로 인해 웹페이지 사용에 제한이 있음</br>
-설정을 바꿔서(true) 자바스크립트 코트를 허용함</br>


(2) WebViewClient</br>
-웹뷰에서 일어나는 요청, 상태, 에러 등 다양한 이벤트의 콜백을 조작할 수 있음</br>

-onPageStarted() //page loading 을 시작했을 때 호출되는 콜백 메소드</br>
-onPageFinished() //page loading 을 끝냈을 때 호출되는 콜백 메소드</br>
-onLoadResource() //파라미터로 넘어온 url 에 의해 특정 리소스를 load 할 때 호출되는 콜백 메소드</br>
-onReceivedError() //request 에 대해 에러가 발생했을 때 호출되는 콜백 메소드. error 변수에 에러에 대한 정보가 담겨져있음</br>
-shouldInterceptRequest()</br>
//resource request 를 가로채서 응답을 내리기 전에 호출되는 메소드</br>
//이 메소드를 활용하여 특정 요청에 대한 필터링 및 응답 값 커스텀 가능</br>
-shouldOverrideUrlLoading()</br>
//현재 웹뷰에 로드될 URL 에 대한 컨트롤을 할 수 있는 메소드</br>

-webView 사용 메서드</br>
`binding.webView.loadUrl(loadingUrl)`</br>
`binding.webView.reload()`</br>
`binding.webView.goForward()`</br>
`binding.webView.goBack()`</br>


(3) WebChromeClient</br>
-브라우저 관점의 이벤트 오버라이드 해서 사용</br>
onProgressChanged()</br>
//현재 페이지 로딩 진행 상황, 0과 100 사이의 정수로 표현.(0% ~ 100%)</br>

(4) 사용자 주소로 이동</br>
-a. addressBar 에는 `android:imeOptions="actionDone"` 속성이 있음</br>

-actionDone //키보드 엔터 버튼 클릭 시 원하는 동작 실행</br>
-normal // 특별한 의미 없음</br>
-actionGo // 이동(웹 브라우저에서 사용)</br>
-actionSearch // 검색</br>
-actionSend // 보내기</br>
-actionNext // 다음</br>
-actionPrevious // 이전(API 11 부터 사용 가능)</br>

-b. setOnEditorActionListener 에 동작 구현
```kotlin
binding.addressBar.setOnEditorActionListener { textView, actionId, keyEvent ->
    //...
}
```


<br></br>
<br></br>

><a id = "content2">**2. swipeRefreshLayout**</a></br>

`implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0`</br>

(1) xml 에서 새로고칠 영역을 swiperefreshlayout 으로 감싼다</br>
```xml
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/refreshLayout">
    <WebView
        android:id="@+id/webView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
```

(2) WebViewClient 에서 페이지 로드가 끝나면 onPageFinished( )에서 `refreshLayout.isRefreshing = false`</br>
```kotlin
inner class WebViewClient: android.webkit.WebViewClient() {
    override fun onPageFinished(view: WebView?, /*redirecting url*/url: String?) {
        super.onPageFinished(view, url)
        //...
        binding.refreshLayout.isRefreshing = false
    }
}
```
<br></br>
<br></br>

><a id = "content3">**3. contentLoadingProgressBar**</a></br>

(1) progressBar 세팅</br>
```xml
//xml
<androidx.core.widget.ContentLoadingProgressBar
            style="@style/Widget.AppCompat.ProgressBar.Horizontal"/>
```

(2) 페이지 로드 중에는 WebChromeClient() 에 onProgressChanged() 를 오버라이딩해줌</br>
newProgress 는 0 ~ 100 사이의 값이며 ` binding.progressBar.progress = newProgress` 로 값에 따라 progressBar UI 업데이트</br>
```kotlin
//MainActivity
inner class WebChromeClient() : android.webkit.WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)

        binding.progressBar.progress = newProgress
    }
}
```
//MainActivity
(3) 페이지를 모두 로드하면 WebViewClient() 에  onPageFinished() 를 오버라이딩해주고 hide() 속성 부여</br>
```kotlin
inner class WebViewClient: android.webkit.WebViewClient() {
    override fun onPageFinished(view: WebView?, /*redirecting url*/url: String?) {
        super.onPageFinished(view, url)
        //...
        binding.progressBar.hide()
    }
}
```
<br></br>
<br></br>
----------
><a id = "ref">**참고링크**</a></br>

TextView Input Type</br>
https://developer.android.com/training/keyboard-input/style</br>
https://developer.android.com/reference/android/widget/TextView#attr_android:inputType

`android: inputType="textUri"`</br>

WebView</br>
https://developer.android.com/guide/webapps/webview</br>

WebChromeClient</br>
http://ankyu.entersoft.kr/Lecture/android/webview_03.asp</br>

WebViewClient</br>
https://readystory.tistory.com/181</br>

swipeRefreshLayout</br>
https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout</br>

progressBar</br>
https://developer.android.com/reference/android/widget/ProgressBar.html</br>

contentLoadingProgressBar</br>
https://developer.android.com/reference/androidx/core/widget/ContentLoadingProgressBar</br>

Status bar 색상 변경</br>
https://origogi.github.io/android/dark-mode/</br>
`<item name="android:windowLightStatusBar">true</item>`</br>

?attr 속성</br>
https://developer.android.com/reference/kotlin/android/R.attr</br>
`android:layout_height="?attr/actionBarSize"`

TextView imeOptions</br>
https://developer.android.com/reference/android/widget/TextView#attr_android:imeOptions</br>
