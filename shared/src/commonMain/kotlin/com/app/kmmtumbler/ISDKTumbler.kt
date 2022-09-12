package com.app.kmmtumbler

import android.webkit.WebResourceRequest
import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.utils.AuthorizationStatus

interface ISDKTumbler {

    @Throws(Exception::class)
    suspend fun authorization(): AuthorizationStatus

    @Throws(Exception::class)
    suspend fun getTokenUser(request: WebResourceRequest): Boolean

    @Throws(Exception::class)
    suspend fun getUserImages(): List<UserBlog>
}
