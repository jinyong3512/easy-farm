package com.capstone.easyfarm

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val prevActivity = intent.getStringExtra("prevActivity")
        if (prevActivity.equals("PestSelectActivity") || prevActivity.equals("ResultActivity"))
            toolbarTextView.text = "도감"
        else if (prevActivity.equals("NaviActivity"))
            toolbarTextView.text = "예보"

        wv1.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.domStorageEnabled = true
        }
        wv1.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
                callback.invoke(origin, true, false)
            }

        }

        wv1.loadUrl(intent.getStringExtra("URL")!!)
    }

    override fun onBackPressed() {
        if (wv1.canGoBack()) {
            wv1.goBack()
        } else {
            finish()
        }
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showProgressBar(true)
        }


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            showProgressBar(false)
        }
    }

    fun showProgressBar(isShow: Boolean) {
        if (isShow) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }
}