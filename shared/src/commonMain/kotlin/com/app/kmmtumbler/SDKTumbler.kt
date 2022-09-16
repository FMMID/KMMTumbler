package com.app.kmmtumbler

import com.app.kmmtumbler.cahe.database.Database
import com.app.kmmtumbler.cahe.database.entities.ImagesEntity
import com.app.kmmtumbler.cahe.database.entities.SubscribersEntity
import com.app.kmmtumbler.cahe.settings.TokensPair
import com.app.kmmtumbler.cahe.settings.TumblerSettings
import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.data.UserImage
import com.app.kmmtumbler.data.UserSubscriber
import com.app.kmmtumbler.network.api.authorization.ITumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.authorization.TumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.user.ITumblerUserAPI
import com.app.kmmtumbler.network.api.user.TumblerUserAPI
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.network.response.ResponseUserSubscribers
import com.app.kmmtumbler.utils.AuthorizationStatus
import com.app.kmmtumbler.utils.parseImage
import io.github.aakira.napier.Napier

class SDKTumbler(databaseDriveFactory: DatabaseDriveFactory) : ISDKTumbler {

    companion object {
        private const val SDK_TUMBLER_LOG = "SDKTumbler"
    }

    private val database = Database(databaseDriveFactory)

    private val settings = TumblerSettings()

    private val tumblerAuthorizationAPI: ITumblerAuthorizationAPI = TumblerAuthorizationAPI()

    private val tumblerUserAPI: ITumblerUserAPI = TumblerUserAPI(settings)

    override suspend fun authorization(): AuthorizationStatus {
        return if (settings.getTokensPair() == null) {
            AuthorizationStatus.Failure(tumblerAuthorizationAPI.authorization())
        } else {
            AuthorizationStatus.Success("Success")
        }
    }

    override suspend fun getTokenUser(code: String): Boolean {
        Napier.v(tag = SDK_TUMBLER_LOG, message = "Here is the authorization code! $code")
        getToken(code).onSuccess {
            settings.setTokensPair(
                tokensPair = TokensPair(
                    it.accessToken,
                    it.refreshToken
                )
            )
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
            val imagesCash = database.getImagesByBlog(blog.uuid)
            val subscribersCash = database.getSubscribersByBlog(blog.uuid)
            val imagesNetwork = tumblerUserAPI.getPosts(blog.uuid).getOrElse {
                Napier.v(tag = SDK_TUMBLER_LOG, message = "getPosts error: $it")
                null
            }
            val subscribersNetwork = tumblerUserAPI.getSubscribers(blog.uuid).getOrElse {
                Napier.v(tag = SDK_TUMBLER_LOG, message = "getSubscribers error: $it")
                null
            }
            UserBlog(
                uuidBlog = blog.uuid,
                images = getImages(blog.uuid, imagesCash, imagesNetwork),
                subscribers = getSubscribers(blog.uuid, subscribersCash, subscribersNetwork)
            )
        }
    }

    private suspend fun getToken(accessCode: String): Result<ResponseToken> {
        return tumblerAuthorizationAPI.getToken(accessCode)
    }

    private fun insertNewUserImages(uuidBlog: String, uriImage: String) {
        database.insertImagesBlog(
            ImagesEntity(
                uuidBlog = uuidBlog,
                uriImage = uriImage
            )
        )
    }

    private fun insertNewUserSubscribers(uuidBlog: String, userSubscriber: UserSubscriber) {
        database.insertSubscribers(
            SubscribersEntity(
                uuid = uuidBlog,
                name = userSubscriber.name,
                url = userSubscriber.url,
                updated = userSubscriber.updated.toLong(),
                following = userSubscriber.following
            )
        )
    }

    private fun getSubscribers(
        uuidBlog: String,
        casSubscriber: List<SubscribersEntity>,
        networkSubscribers: ResponseUserSubscribers?
    ): List<UserSubscriber> {
        return when {
            networkSubscribers != null && networkSubscribers.response.users.size >= casSubscriber.size -> {
                networkSubscribers.response.users.map {
                    val data = UserSubscriber(
                        name = it.name,
                        url = it.url,
                        updated = it.updated,
                        following = it.following
                    )
                    insertNewUserSubscribers(uuidBlog, data)
                    data
                }
            }
            networkSubscribers == null && casSubscriber.isNotEmpty() -> {
                casSubscriber.map {
                    UserSubscriber(
                        name = it.name,
                        url = it.url,
                        updated = it.updated.toInt(),
                        following = it.following
                    )
                }
            }
            else -> listOf()
        }
    }

    private fun getImages(
        uuidBlog: String,
        databaseCash: List<ImagesEntity>,
        networkData: ResponseUserPosts?
    ): List<UserImage> {
        return when {
            networkData != null && networkData.response.blog.size >= databaseCash.size -> {
                networkData.response.blog.map {
                    val parsedBody = it.body.parseImage() ?: ""
                    insertNewUserImages(uuidBlog, parsedBody)
                    UserImage(uri = parsedBody)
                }
            }
            networkData == null && databaseCash.isNotEmpty() -> {
                databaseCash.map {
                    UserImage(uri = it.uriImage)
                }
            }
            else -> listOf()
        }
    }
}
