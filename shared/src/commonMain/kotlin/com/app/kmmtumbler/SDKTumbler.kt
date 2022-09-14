package com.app.kmmtumbler

import com.app.kmmtumbler.cahe.Database
import com.app.kmmtumbler.cahe.TumblerAuthorizationDAO
import com.app.kmmtumbler.cahe.entities.ImagesEntity
import com.app.kmmtumbler.cahe.entities.TokensEntity
import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.data.UserImage
import com.app.kmmtumbler.network.api.authorization.ITumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.authorization.TumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.user.ITumblerUserAPI
import com.app.kmmtumbler.network.api.user.TumblerUserAPI
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.utils.AuthorizationStatus
import com.app.kmmtumbler.utils.parseImage
import io.github.aakira.napier.Napier

class SDKTumbler(databaseDriveFactory: DatabaseDriveFactory) : ISDKTumbler {

    private val database = Database(databaseDriveFactory)

    private val tumblerAuthorizationAPI: ITumblerAuthorizationAPI = TumblerAuthorizationAPI()

    private val tumblerUserAPI: ITumblerUserAPI = TumblerUserAPI(TumblerAuthorizationDAO(::getActualTokensPair))

    override suspend fun authorization(): AuthorizationStatus {
        return if (database.getAllTokens().isEmpty()) {
            AuthorizationStatus.Failure(tumblerAuthorizationAPI.authorization())
        } else {
            AuthorizationStatus.Success("Success")
        }
    }

    override suspend fun getTokenUser(code: String): Boolean {
        Napier.v(tag = "OAuth", message = "Here is the authorization code! $code")
        kotlin.runCatching {
            getToken(code)
        }.onSuccess {
            database.insertNewToken(
                TokensEntity(
                    id = 0L,
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            )
            return true
        }.onFailure {
            Napier.v(tag = "Token", message = "token response: ${it.message}")
            return false
        }
        return true
    }

    override suspend fun getUserImages(): List<UserBlog> {
        return tumblerUserAPI.getUserInfo().responseUserBlogsData.userBlogsData.blogsData.map { blog ->
            if (database.getImagesByBlog(blog.uuid).isNotEmpty()) {
                UserBlog(
                    uuidBlog = blog.uuid,
                    images = database.getImagesByBlog(blog.uuid).map {
                        UserImage(uri = it.uriImage)
                    }
                )
            } else {
                UserBlog(
                    uuidBlog = blog.uuid,
                    images = tumblerUserAPI.getPosts(blog.uuid).response.blog.map { post ->
                        database.insertImagesBlog(
                            ImagesEntity(
                                id = 0L,
                                uuidBlog = blog.uuid,
                                uriImage = post.body.parseImage() ?: ""
                            )
                        )
                        UserImage(uri = post.body.parseImage() ?: "")
                    }
                )
            }
        }
    }

    private suspend fun getToken(accessCode: String): ResponseToken {
        return tumblerAuthorizationAPI.getToken(accessCode)
    }

    private fun getActualTokensPair(): TokensEntity? {
        return database.getAllTokens().firstOrNull()
    }
}
