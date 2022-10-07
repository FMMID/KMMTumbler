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
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kmmtumbler.TumblerPublicConfig
import com.app.kmmtumbler.models.TumblerViewModel
import com.app.kmmtumbler.network.api.authorization.TumblerAuthorizationAPI
import com.app.kmmtumbler.utils.AuthorizationStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()
    private lateinit var webView: WebView
    private lateinit var text: TextView
    private lateinit var pagingButton: Button
    private lateinit var pagingRecyclerView: RecyclerView
    private val tumblerViewModel: TumblerViewModel by inject()

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagingButton = findViewById(R.id.pagingButton)
        pagingRecyclerView = findViewById(R.id.pagignRecyclerView)
        text = findViewById(R.id.mainText)
        webView = findViewById(R.id.webView)

        text.text = "Loading..."
        pagingRecyclerView.layoutManager = LinearLayoutManager(this)
        pagingButton.visibility = View.INVISIBLE
        pagingRecyclerView.visibility = View.INVISIBLE

        pagingButton.setOnClickListener {
            pagingRecyclerView.visibility = View.VISIBLE
            pagingButton.visibility = View.INVISIBLE
            text.visibility = View.INVISIBLE
            val adapter = UserFollowingDataAdapter()
            pagingRecyclerView.adapter = adapter
            tumblerViewModel.pagingFollowingController?.pagingDataAndroid?.onEach { adapter.submitData(it) }
                ?.launchIn(scope)
        }

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
                        }.onSuccess { successCheck ->
                            if (successCheck) {
                                view?.visibility = View.INVISIBLE
                                tumblerViewModel.getUserData().let { text.text = it.toString() }
                                pagingButton.visibility = View.VISIBLE
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

        scope.launch {
            when (val result = tumblerViewModel.authorization()) {
                is AuthorizationStatus.Success -> {
                    webView.visibility = View.INVISIBLE
                    pagingButton.visibility = View.VISIBLE
                    tumblerViewModel.getUserData().let { text.text = it.toString() }
                }
                is AuthorizationStatus.Failure -> {
                    webView.loadUrl(result.value)
                }
            }
        }
    }

    private suspend fun checkUrl(request: WebResourceRequest): Boolean {
        if (request.url.toString().startsWith(TumblerPublicConfig.REDIRECT_URI) &&
            request.url.getQueryParameter("state") == TumblerAuthorizationAPI.SESSION_STATE
        ) {
            request.url.getQueryParameter("code")?.let { code ->
                Log.d("OAuth", "Here is the authorization code! $code")
                return tumblerViewModel.getTokenUser(code)
            } ?: run {
                Log.d("OAuth", "Authorization code not received :(")
            }
        }
        return false
    }
}
