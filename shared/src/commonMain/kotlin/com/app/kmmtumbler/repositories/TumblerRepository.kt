package com.app.kmmtumbler.repositories

import com.app.kmmtumbler.cahe.database.Database
import com.app.kmmtumbler.cahe.database.cashentities.CashImagesEntity
import com.app.kmmtumbler.cahe.database.cashentities.CashSubscribersEntity
import com.app.kmmtumbler.cahe.settings.TokensPair
import com.app.kmmtumbler.cahe.settings.TumblerSettings
import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.data.UserImage
import com.app.kmmtumbler.data.UserSubscriber
import com.app.kmmtumbler.network.api.authorization.ITumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.user.ITumblerUserAPI
import com.app.kmmtumbler.network.response.ResponseToken
import com.app.kmmtumbler.network.response.ResponseUserPosts
import com.app.kmmtumbler.network.response.ResponseUserSubscribers
import com.app.kmmtumbler.utils.AuthorizationStatus
import com.app.kmmtumbler.utils.getUserData
import io.github.aakira.napier.Napier

class TumblerRepository(
    private val database: Database,
    private val settings: TumblerSettings,
    private val tumblerAuthorizationAPI: ITumblerAuthorizationAPI,
    private val tumblerUserAPI: ITumblerUserAPI
) : ITumblerRepository {

    companion object {
        private const val SDK_TUMBLER_LOG = "SDKTumbler"
    }

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

    override suspend fun getUserData(): List<UserBlog> {
        return tumblerUserAPI.getUserInfo().getOrElse {
            Napier.v(tag = SDK_TUMBLER_LOG, message = "getUserInfo error: $it")
            return listOf()
        }.responseUserBlogsData.userBlogsData.blogsData.map { blog ->
            val imagesCash = CashImagesEntity(database.getImagesByBlog(blog.uuid))
            val subscribersCash = CashSubscribersEntity(database.getSubscribersByBlog(blog.uuid))
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
                images = getUserData<CashImagesEntity, ResponseUserPosts, UserImage>(
                    uuidBlog = blog.uuid,
                    cash = imagesCash,
                    network = imagesNetwork,
                    saveDataCallback = database::insertNewUserImages
                ),
                subscribers = getUserData<CashSubscribersEntity, ResponseUserSubscribers, UserSubscriber>(
                    uuidBlog = blog.uuid,
                    cash = subscribersCash,
                    network = subscribersNetwork,
                    saveDataCallback = database::insertNewUserSubscribers
                )
            )
        }
    }

    private suspend fun getToken(accessCode: String): Result<ResponseToken> {
        return tumblerAuthorizationAPI.getToken(accessCode)
    }
}