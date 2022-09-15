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

    companion object {
        private const val SDK_TUMBLER_LOG = "SDKTumbler"
    }

    private val database = Database(databaseDriveFactory)

    private val tumblerAuthorizationAPI: ITumblerAuthorizationAPI = TumblerAuthorizationAPI()

    private val tumblerUserAPI: ITumblerUserAPI =
        TumblerUserAPI(TumblerAuthorizationDAO(::getActualTokensPair, ::insertNewToken))

    override suspend fun authorization(): AuthorizationStatus {
        return if (database.getAllTokens().isEmpty()) {
            AuthorizationStatus.Failure(tumblerAuthorizationAPI.authorization())
        } else {
            AuthorizationStatus.Success("Success")
        }
    }

    override suspend fun getTokenUser(code: String): Boolean {
        Napier.v(tag = SDK_TUMBLER_LOG, message = "Here is the authorization code! $code")
        getToken(code).onSuccess {
            insertNewToken(it)
        }.onFailure {
            Napier.v(tag = SDK_TUMBLER_LOG, message = "token response: ${it.message}")
            return false
        }
        return true
    }

    override suspend fun getUserImages(): List<UserBlog> {
        return tumblerUserAPI.getUserInfo().getOrElse {
            Napier.v(tag = SDK_TUMBLER_LOG, message = "getUserInfo error: $it")
            return listOf()
        }.responseUserBlogsData.userBlogsData.blogsData.map { blog ->
            val databaseCash = database.getImagesByBlog(blog.uuid)
            val networkData = tumblerUserAPI.getPosts(blog.uuid).getOrElse {
                Napier.v(tag = SDK_TUMBLER_LOG, message = "getPosts error: $it")
                null
            }
            when {
                networkData != null && networkData.response.blog.size >= databaseCash.size -> {
                    UserBlog(
                        uuidBlog = blog.uuid,
                        images = networkData.response.blog.map { post ->
                            val parsedPost = post.body.parseImage() ?: ""
                            insertNewUserImages(blog.uuid, parsedPost)
                            UserImage(parsedPost)
                        }
                    )
                }
                networkData == null && databaseCash.isNotEmpty() -> {
                    UserBlog(
                        uuidBlog = blog.uuid,
                        images = database.getImagesByBlog(blog.uuid).map { UserImage(uri = it.uriImage) }
                    )
                }
                else -> UserBlog(
                    uuidBlog = blog.uuid,
                    images = listOf()
                )
            }
        }
    }

    private suspend fun getToken(accessCode: String): Result<ResponseToken> {
        return tumblerAuthorizationAPI.getToken(accessCode)
    }

    private fun getActualTokensPair(): TokensEntity? {
        return database.getAllTokens().firstOrNull()
    }

    private fun insertNewToken(responseToken: ResponseToken) {
        database.insertNewToken(
            TokensEntity(
                accessToken = responseToken.accessToken,
                refreshToken = responseToken.refreshToken
            )
        )
    }

    private fun insertNewUserImages(uuidBlog: String, uriImage: String) {
        database.insertImagesBlog(
            ImagesEntity(
                uuidBlog = uuidBlog,
                uriImage = uriImage
            )
        )
    }

    private fun insertNewToken(accessToken: String, refreshToken: String) {
        database.insertNewToken(
            TokensEntity(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }
}
