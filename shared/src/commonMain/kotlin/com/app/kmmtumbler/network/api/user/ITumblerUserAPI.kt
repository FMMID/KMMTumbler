package com.app.kmmtumbler.network.api.user

import com.app.kmmtumbler.network.response.ResponseUserInfo
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.network.response.ResponseUserSubscribers

interface ITumblerUserAPI {

    suspend fun getUserInfo(): Result<ResponseUserInfo>

    suspend fun getPosts(uuidBlog: String): Result<ResponseUserPosts>

    suspend fun getSubscribers(uuidBlog: String): Result<ResponseUserSubscribers>
}