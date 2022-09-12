package com.app.kmmtumbler.data


data class UserBlog(

    val uuidBlog: String,
    val images: List<UserImage>
)

data class UserImage(
    val uri: String
)
