package com.app.kmmtumbler.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserPosts(

    @SerialName("response")
    val response: ResponsePosts
)

@Serializable
data class ResponsePosts(

    @SerialName("posts")
    val blog: List<ResponsePostsBlogPost>
)


@Serializable
data class ResponsePostsBlogPost(

    @SerialName("body")
    val body: String
)
