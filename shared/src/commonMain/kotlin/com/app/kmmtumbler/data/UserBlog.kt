package com.app.kmmtumbler.data

import com.app.kmmtumbler.utils.Result

data class UserBlog(
    val uuidBlog: String,
    val images: List<UserImage>,
    val subscribers: List<UserSubscriber>
)