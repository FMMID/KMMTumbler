package com.app.kmmtumbler.network.response

import com.app.kmmtumbler.data.UserImage
import com.app.kmmtumbler.utils.Network
import com.app.kmmtumbler.utils.Result
import com.app.kmmtumbler.utils.parseImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserPosts(

    @SerialName("response")
    val response: ResponsePosts
) : Network {

    override fun getData(): List<Any> {
        return response.blog
    }

    override fun mapToResult(): List<Result> {
        return response.blog.map {
            val parsedBody = it.body.parseImage() ?: ""
            UserImage(uri = parsedBody)
        }
    }
}

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
