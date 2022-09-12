package com.app.kmmtumbler.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserInfo(

    @SerialName("response")
    val responseUserBlogsData: ResponseUser
)

@Serializable
data class ResponseUser(

    @SerialName("user")
    val userBlogsData: ResponseUserBlogs
)

@Serializable
data class ResponseUserBlogs(

    @SerialName("blogs")
    val blogsData: List<ResponseUserBlogsData>
)

@Serializable
data class ResponseUserBlogsData(

    @SerialName("uuid")
    val uuid: String
)
