package com.app.kmmtumbler.data

import com.app.kmmtumbler.cahe.entities.ImagesEntity
import com.app.kmmtumbler.cahe.entities.SubscribersEntity
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.network.response.ResponseUserSubscribers
import com.app.kmmtumbler.utils.parseImage


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
