package com.app.kmmtumbler

import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.utils.AuthorizationStatus

interface ISDKTumbler {

    @Throws(Exception::class)
    suspend fun authorization(): AuthorizationStatus

    @Throws(Exception::class)
    suspend fun getTokenUser(code: String): Boolean

    @Throws(Exception::class)
    suspend fun getUserImages(): List<UserBlog>
}
