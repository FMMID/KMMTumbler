package com.app.kmmtumbler.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseToken(

    @SerialName("access_token")
    val accessToken: String,

    @SerialName("refresh_token")
    val refreshToken: String,

    @SerialName("expires_in")
    val expiresIn: Long,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("scope")
    val scope: String,

    @SerialName("id_token")
    val idToken: Boolean,
)
