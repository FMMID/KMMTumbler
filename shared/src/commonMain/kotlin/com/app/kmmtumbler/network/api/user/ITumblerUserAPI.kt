package com.app.kmmtumbler.network.api.user

import com.app.kmmtumbler.network.request.RequestUserFollowing
import com.app.kmmtumbler.network.response.ResponseUserFollowing
import com.app.kmmtumbler.network.response.ResponseUserInfo
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.network.response.ResponseUserSubscribers

interface ITumblerUserAPI {

    suspend fun getUserInfo(): Result<ResponseUserInfo>

    suspend fun getPosts(uuidBlog: String): Result<ResponseUserPosts>

    suspend fun getSubscribers(uuidBlog: String): Result<ResponseUserSubscribers>

    suspend fun getFollowing(uuidBlog: String, request:RequestUserFollowing): Result<ResponseUserFollowing>
}