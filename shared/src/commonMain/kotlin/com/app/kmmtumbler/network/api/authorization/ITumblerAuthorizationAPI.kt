package com.app.kmmtumbler.network.api.authorization

import com.app.kmmtumbler.network.response.ResponseToken

interface ITumblerAuthorizationAPI {

    suspend fun authorization(): String

    suspend fun getToken(accessCode: String): ResponseToken
}