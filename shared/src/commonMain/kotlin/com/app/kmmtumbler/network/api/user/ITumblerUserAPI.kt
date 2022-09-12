package com.app.kmmtumbler.network.api.user

import com.app.kmmtumbler.network.response.ResponseUserInfo
import com.app.kmmtumbler.network.response.ResponseUserPosts

interface ITumblerUserAPI {

    suspend fun getUserInfo(): ResponseUserInfo

    suspend fun getPosts(uuidBlog: String): ResponseUserPosts
}