package com.app.kmmtumbler.cahe

import com.app.kmmtumbler.DatabaseDriveFactory
import com.app.kmmtumbler.cahe.entities.ImagesEntity
import com.app.kmmtumbler.cahe.entities.SubscribersEntity
import com.app.kmmtumbler.cahe.entities.TokensEntity
import com.app.kmmtumbler.shared.cache.TumblerDatabase

internal class Database(databaseDriveFactory: DatabaseDriveFactory) {

    private val database = TumblerDatabase(databaseDriveFactory.createDriver())
    private val dbQuery = database.tumblerDatabaseQueries

    internal fun getAllTokens(): List<TokensEntity> {
        return dbQuery.selectAllTokenPair(::mapTokens).executeAsList()
    }

    internal fun getImagesByBlog(uuid: String): List<ImagesEntity> {
        return dbQuery.selectAllImagesByBlog(uuid, mapper = ::mapImages).executeAsList()
    }

    internal fun getSubscribersByBlog(uuid: String): List<SubscribersEntity> {
        return dbQuery.selectAllSubscribers(uuid, mapper = ::mapSubscribers).executeAsList()
    }

    internal fun insertNewToken(tokenEntity: TokensEntity) {
        dbQuery.transaction {
            val list = dbQuery.selectAllTokenPair(::mapTokens).executeAsList()
            if (list.find { it.accessToken == tokenEntity.accessToken } == null) {
                dbQuery.insertAuthorizationToken(tokenEntity.accessToken, tokenEntity.refreshToken)
            }
        }
    }

    internal fun insertImagesBlog(imagesEntity: ImagesEntity) {
        dbQuery.transaction {
            val list = dbQuery.selectAllImagesByBlog(imagesEntity.uuidBlog).executeAsList()
            if (list.find { it.imageUri == imagesEntity.uriImage } == null) {
                dbQuery.insertImagesBlog(imagesEntity.uuidBlog, imagesEntity.uriImage)
            }
        }
    }

    internal fun insertSubscribers(subscribersEntity: SubscribersEntity) {
        dbQuery.transaction {
            val list = dbQuery.selectAllSubscribers(subscribersEntity.uuid).executeAsList()
            if (list.find { it.url == subscribersEntity.url } == null) {
                dbQuery.insertSubscribersBlog(
                    subscribersEntity.uuid,
                    subscribersEntity.name,
                    subscribersEntity.url,
                    subscribersEntity.updated,
                    if (subscribersEntity.following) 1L else 0L
                )
            }
        }
    }

    private fun mapTokens(
        id: Long,
        accessToken: String,
        refreshToken: String
    ): TokensEntity = TokensEntity(
        id = id,
        accessToken = accessToken,
        refreshToken = refreshToken
    )

    private fun mapImages(
        id: Long,
        blog: String,
        uriImage: String
    ): ImagesEntity = ImagesEntity(
        id = id,
        uriImage = uriImage,
        uuidBlog = blog
    )

    private fun mapSubscribers(
        id: Long,
        uuid: String,
        name: String,
        url: String,
        updated: Long,
        following: Long
    ): SubscribersEntity = SubscribersEntity(
        id = id,
        uuid = uuid,
        name = name,
        url = url,
        updated = updated,
        following = following == 1L
    )
}
