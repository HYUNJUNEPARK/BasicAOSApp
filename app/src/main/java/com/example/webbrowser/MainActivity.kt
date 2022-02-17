package com.example.webbrowser

import android.graphics.Bitmap
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.webbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    companion object {
        private const val DEFAULT_URL = "https://www.google.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    override fun onBackPressed() { //액티비티에서 백버튼을 눌럿을 때
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun initViews() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews(){
        binding.addressBar.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = textView.text.toString()

                if (URLUtil.isNetworkUrl(loadingUrl)) {
                    binding.webView.loadUrl(loadingUrl)
                } else {
                    binding.webView.loadUrl("http://$loadingUrl")
                }
            }
            return@setOnEditorActionListener false
        }
        binding.goBackButton.setOnClickListener {
            binding.webView.goBack()
        }
        binding.goForwardButton.setOnClickListener {
            binding.webView.goForward()
        }
        binding.goHomeButton.setOnClickListener {
            binding.webView.loadUrl(DEFAULT_URL)
        }
        binding.refreshLayout.setOnRefreshListener {
            binding.webView.reload()
        }
    }
    //inner 를 붙혀줘야 MainActivity 에 있는 속성에 접근할 수 있음
    inner class WebViewClient: android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            binding.progressBar.show()
        }

        override fun onPageFinished(view: WebView?, /*redirecting url*/url: String?) {
            super.onPageFinished(view, url)

            binding.refreshLayout.isRefreshing = false
            binding.progressBar.hide()
            binding.goBackButton.isEnabled = binding.webView.canGoBack()
            binding.goForwardButton.isEnabled = binding.webView.canGoForward()
            binding.addressBar.setText(url)
        }
    }

    inner class WebChromeClient() : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            binding.progressBar.progress = newProgress
        }
    }

}
