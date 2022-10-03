package com.app.kmmtumbler.repositories

import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.utils.AuthorizationStatus

interface ITumblerRepository {

    @Throws(Exception::class)
    suspend fun authorization(): AuthorizationStatus

    @Throws(Exception::class)
    suspend fun getTokenUser(code: String): Boolean

    @Throws(Exception::class)
    suspend fun getUserData(): List<UserBlog>
}
