package com.example.webbrowser

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import com.example.webbrowser.databinding.ActivityMainBinding
import com.example.webbrowser.webview.Constants.Companion.DEFAULT_URL
import com.example.webbrowser.webview.WebView

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initWebView()
        bindViews()
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun initWebView() {
        binding.webView.apply {
            val webView = WebView(binding)
            webViewClient = webView.WebViewClient()
            webChromeClient = webView.WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews(){
        binding.apply {
            addressBar.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val loadingUrl = textView.text.toString()
                    if (URLUtil.isNetworkUrl(loadingUrl)) { //true if the url is a network url
                        binding.webView.loadUrl(loadingUrl)
                    }
                    else {
                        binding.webView.loadUrl("http://$loadingUrl")
                    }
                }
                return@setOnEditorActionListener false
            }
            goBackButton.setOnClickListener { binding.webView.goBack() }
            goForwardButton.setOnClickListener { binding.webView.goForward() }
            goHomeButton.setOnClickListener { binding.webView.loadUrl(DEFAULT_URL) }
            refreshLayout.setOnRefreshListener { binding.webView.reload() }
        }
    }
}
