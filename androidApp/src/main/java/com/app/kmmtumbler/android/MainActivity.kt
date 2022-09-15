package com.app.kmmtumbler.android

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.kmmtumbler.DatabaseDriveFactory
import com.app.kmmtumbler.ISDKTumbler
import com.app.kmmtumbler.SDKTumbler
import com.app.kmmtumbler.network.api.authorization.TumblerAuthorizationAPI
import com.app.kmmtumbler.utils.AuthorizationStatus
import com.app.kmmtumbler.utils.CommonConst
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()

    private lateinit var webView: WebView
    private lateinit var tv: TextView
    private lateinit var tumblerSDK: ISDKTumbler

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tumblerSDK = SDKTumbler(DatabaseDriveFactory(this))

        tv = findViewById(R.id.text_view)
        webView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {

            var progressDialog: ProgressDialog? = ProgressDialog(webView.context)

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressDialog?.setTitle("Loading...")
                progressDialog?.setMessage("Please wait...")
                progressDialog?.setCancelable(false)
                progressDialog?.show()
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progressDialog?.dismiss()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.let {
                    scope.launch {
                        kotlin.runCatching {
                            checkUrl(it)
                        }.onSuccess {
                            if (it) {
                                view?.visibility = View.INVISIBLE
                                tv.text = tumblerSDK.getUserImages().toString()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webView.settings.apply {
            defaultTextEncodingName = "utf-8"
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        tv.text = "Loading..."

        scope.launch {
            kotlin.runCatching {
                tumblerSDK.authorization()
            }.onSuccess {
                when (it) {
                    is AuthorizationStatus.Success -> {
                        webView.visibility = View.INVISIBLE
                        tv.text = tumblerSDK.getUserImages().toString()
                    }
                    is AuthorizationStatus.Failure -> {
                        webView.loadUrl(it.value)
                    }
                }
            }.onFailure {
                webView.visibility = View.INVISIBLE
                tv.text = it.localizedMessage
            }
        }
    }

    private suspend fun checkUrl(request: WebResourceRequest): Boolean {
        if (request.url.toString().startsWith(CommonConst.REDIRECT_URI) &&
            request.url.getQueryParameter("state") == TumblerAuthorizationAPI.SESSION_STATE
        ) {
            request.url.getQueryParameter("code")?.let { code ->
                Log.d("OAuth", "Here is the authorization code! $code")
                return tumblerSDK.getTokenUser(code)
            } ?: run {
                Log.d("OAuth", "Authorization code not received :(")
                return false
            }
        }
        return false
    }
}
