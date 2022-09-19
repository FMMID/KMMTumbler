package com.app.kmmtumbler.network.response

import com.app.kmmtumbler.data.UserSubscriber
import com.app.kmmtumbler.utils.Network
import com.app.kmmtumbler.utils.Result
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserSubscribers(

    @SerialName("response")
    val response:ResponseSubscribers
) : Network {

    override fun getData(): List<Any> {
        return response.users
    }

    override fun mapToResult(): List<Result> {
        return response.users.map {
            UserSubscriber(
                name = it.name,
                url = it.url,
                updated = it.updated,
                following = it.following
            )
        }
    }
}

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