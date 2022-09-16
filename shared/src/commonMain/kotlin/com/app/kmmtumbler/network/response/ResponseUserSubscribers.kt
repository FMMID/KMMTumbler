package com.app.kmmtumbler.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserSubscribers(

    @SerialName("response")
    val response:ResponseSubscribers
)

@Serializable
data class ResponseSubscribers(

    @SerialName("users")
    val users: List<ResponseSubscribersUser>
)

@Serializable
data class ResponseSubscribersUser(

    @SerialName("name")
    val name:String,

    @SerialName("url")
    val url:String,

    @SerialName("updated")
    val updated:Int,

    @SerialName("following")
    val following: Boolean
)