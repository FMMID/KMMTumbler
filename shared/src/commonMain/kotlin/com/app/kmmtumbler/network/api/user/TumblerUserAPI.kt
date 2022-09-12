package com.app.kmmtumbler.network.api.user

import com.app.kmmtumbler.cahe.ITumblerAuthorizationDAO
import com.app.kmmtumbler.network.request.RequestRefreshToken
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.network.response.ResponseUserInfo
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.utils.CommonCosts
import com.app.kmmtumbler.utils.getDefaultHttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class TumblerUserAPI(private val authorizationDAO: ITumblerAuthorizationDAO) : ITumblerUserAPI {

    companion object {
        const val HOST_NAME_TUMBLER_API = "api.tumblr.com"
        const val TUMBLER_API_VERSION = "v2"
        const val LOGGING_TAG = "HTTP Client / User API"
    }

    private val httpClient = getDefaultHttpClient(LOGGING_TAG).config {
        install(Auth) {
            bearer {
                loadTokens {
                    val lastActualToken = authorizationDAO.getActualTokensPair()
                    if (lastActualToken != null) {
                        BearerTokens(lastActualToken.accessToken, lastActualToken.refreshToken)
                    } else {
                        BearerTokens("", "")
                    }
                }
                refreshTokens {
                    val lastActualToken = authorizationDAO.getActualTokensPair()
                    if (lastActualToken != null) {
                        val result = refreshToken(lastActualToken.refreshToken)
                        BearerTokens(result.accessToken, result.refreshToken)
                    } else {
                        BearerTokens("", "")
                    }
                }
            }
        }
    }

    private suspend fun refreshToken(refreshToken: String): ResponseToken {
        return getDefaultHttpClient(LOGGING_TAG).post {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST_NAME_TUMBLER_API
                path("${TUMBLER_API_VERSION}/oauth2/token")
            }
            contentType(ContentType.Application.Json)
            setBody(
                RequestRefreshToken(
                    grantType = "refresh_token",
                    clientId = CommonCosts.CLIENT_CONSUMER_KEY,
                    clientSecret = CommonCosts.CLIENT_SECRET_KEY,
                    refreshToken = refreshToken
                )
            )
        }.body()
    }

    override suspend fun getUserInfo(): ResponseUserInfo {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST_NAME_TUMBLER_API
                path("${TUMBLER_API_VERSION}/user/info")
            }
        }.body()
    }

    override suspend fun getPosts(uuidBlog: String): ResponseUserPosts {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST_NAME_TUMBLER_API
                path("${TUMBLER_API_VERSION}/blog/$uuidBlog/posts")
            }
        }.body()
    }
}
