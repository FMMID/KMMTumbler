package com.app.kmmtumbler.network.api.authorization

import com.app.kmmtumbler.getUniqueState
import com.app.kmmtumbler.network.request.RequestToken
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.utils.CommonConst.CLIENT_CONSUMER_KEY
import com.app.kmmtumbler.utils.CommonConst.CLIENT_SECRET_KEY
import com.app.kmmtumbler.utils.CommonConst.REDIRECT_URI
import com.app.kmmtumbler.utils.getDefaultHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class TumblerAuthorizationAPI : ITumblerAuthorizationAPI {

    companion object {
        const val HOST_NAME_TUMBLER = "www.tumblr.com"
        const val HOST_NAME_TUMBLER_API = "api.tumblr.com"
        const val TUMBLER_API_VERSION = "v2"
        const val LOGGING_TAG = "HTTP Client / Authorization API"
        val SESSION_STATE = getUniqueState()
    }

    private val httpClient = getDefaultHttpClient(LOGGING_TAG)

    override suspend fun authorization(): String {
        return URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = HOST_NAME_TUMBLER,
            parameters = Parameters.build {
                append("client_id", CLIENT_CONSUMER_KEY)
                append("response_type", "code")
                append("scope", "offline_access")
                append("state", SESSION_STATE)
                append("redirect_uri", REDIRECT_URI)
            },
        ).apply { path("oauth2/authorize") }.buildString()
    }

    override suspend fun getToken(accessCode: String): ResponseToken {
        return httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST_NAME_TUMBLER_API
                path("$TUMBLER_API_VERSION/oauth2/token")
            }
            contentType(ContentType.Application.Json)
            setBody(
                RequestToken(
                    grantType = "authorization_code",
                    code = accessCode,
                    clientId = CLIENT_CONSUMER_KEY,
                    clientSecret = CLIENT_SECRET_KEY,
                    redirectUri = REDIRECT_URI
                )
            )
        }.body()
    }
}
