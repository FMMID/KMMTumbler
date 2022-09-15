package com.app.kmmtumbler.network.api.user

import com.app.kmmtumbler.cahe.ITumblerAuthorizationDAO
import com.app.kmmtumbler.network.request.RequestRefreshToken
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.network.response.ResponseUserInfo
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.utils.CommonConst
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
                val defaultBearToken = BearerTokens("", "")
                loadTokens {
                    val lastActualToken = authorizationDAO.getActualTokensPair() ?: return@loadTokens defaultBearToken
                    BearerTokens(lastActualToken.accessToken, lastActualToken.refreshToken)
                }
                refreshTokens {
                    val lastActualToken =
                        authorizationDAO.getActualTokensPair() ?: return@refreshTokens defaultBearToken
                    refreshToken(lastActualToken.refreshToken).getOrElse {
                        return@refreshTokens defaultBearToken
                    }.let {
                        authorizationDAO.insertNewTokensPair(it.accessToken, it.refreshToken)
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }
            }
        }
    }

    private suspend fun refreshToken(refreshToken: String): Result<ResponseToken> {
        return try {
            Result.success(
                getDefaultHttpClient(LOGGING_TAG).post {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = HOST_NAME_TUMBLER_API
                        path("${TUMBLER_API_VERSION}/oauth2/token")
                    }
                    contentType(ContentType.Application.Json)
                    setBody(
                        RequestRefreshToken(
                            grantType = "refresh_token",
                            clientId = CommonConst.CLIENT_CONSUMER_KEY,
                            clientSecret = CommonConst.CLIENT_SECRET_KEY,
                            refreshToken = refreshToken
                        )
                    )
                }.body()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserInfo(): Result<ResponseUserInfo> {
        return try {
            Result.success(httpClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST_NAME_TUMBLER_API
                    path("${TUMBLER_API_VERSION}/user/info")
                }
            }.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPosts(uuidBlog: String): Result<ResponseUserPosts> {
        return try {
            Result.success(
                httpClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = HOST_NAME_TUMBLER_API
                        path("${TUMBLER_API_VERSION}/blog/$uuidBlog/posts")
                    }
                }.body()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
