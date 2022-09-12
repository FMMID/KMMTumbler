package com.app.kmmtumbler.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestToken(

    @SerialName("grant_type")
    val grantType: String,

    @SerialName("code")
    val code: String,

    @SerialName("client_id")
    val clientId: String,

    @SerialName("client_secret")
    val clientSecret: String,

    @SerialName("redirect_uri")
    val redirectUri: String
)
