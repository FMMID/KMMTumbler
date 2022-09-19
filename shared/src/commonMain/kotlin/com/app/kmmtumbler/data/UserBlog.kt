package com.app.kmmtumbler.data

import com.app.kmmtumbler.utils.Result

data class UserBlog(
    val uuidBlog: String,
    val images: List<UserImage>,
    val subscribers: List<UserSubscriber>
)

data class UserSubscriber(
    val name: String,
    val url: String,
    val updated: Int,
    val following: Boolean
): Result

data class UserImage(
    val uri: String
) : Result
