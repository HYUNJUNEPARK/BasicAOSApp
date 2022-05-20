package com.example.webbrowser.webview

import android.graphics.Bitmap
import android.webkit.WebView
import com.example.webbrowser.databinding.ActivityMainBinding

class WebView(private val binding: ActivityMainBinding) {
    inner class WebViewClient(): android.webkit.WebViewClient() {
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
        override fun onProgressChanged(view: WebView?, newProgress: Int/*0 - 100*/) {
            super.onProgressChanged(view, newProgress)

            binding.progressBar.progress = newProgress
        }
    }
}