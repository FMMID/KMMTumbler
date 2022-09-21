package com.app.kmmtumbler.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserFollowing(

    @SerialName("response")
    val response: ResponseFollowing
)

@Serializable
data class ResponseFollowing(

    @SerialName("blogs")
    val blogs: List<ResponseFollowingBlogs>,

    @SerialName("_links")
    val links: ResponseFollowingLinks,

    @SerialName("total_blogs")
    val blogSize: Int
)

@Serializable
data class ResponseFollowingBlogs(

    @SerialName("name")
    val name: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("url")
    val url: String,

    @SerialName("uuid")
    val uuid: String,

    @SerialName("updated")
    val updated: Long,
)

@Serializable
data class ResponseFollowingLinks(

    @SerialName("next")
    val next: ResponseFollowingLinksNext
)

@Serializable
data class ResponseFollowingLinksNext(

    @SerialName("query_params")
    val params: ResponseFollowingLinksNextQueryParams
)

@Serializable
data class ResponseFollowingLinksNextQueryParams(

    @SerialName("limit")
    val limit: String,

    @SerialName("offset")
    val offset: String
)