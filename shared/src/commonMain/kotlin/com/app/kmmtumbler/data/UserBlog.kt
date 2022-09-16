package com.app.kmmtumbler.data

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
)

data class UserImage(
    val uri: String
)
