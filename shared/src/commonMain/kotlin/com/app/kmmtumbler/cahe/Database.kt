package com.app.kmmtumbler.cahe

import com.app.kmmtumbler.DatabaseDriveFactory
import com.app.kmmtumbler.cahe.entities.ImagesEntity
import com.app.kmmtumbler.cahe.entities.TokensEntity
import com.app.kmmtumbler.shared.cache.TumblerDatabase

internal class Database(databaseDriveFactory: DatabaseDriveFactory) {

    private val database = TumblerDatabase(databaseDriveFactory.createDriver())
    private val dbQuery = database.tumblerDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.removeAllAuthorizationToken()
        dbQuery.removeAllImages()
    }

    internal fun getAllTokens(): List<TokensEntity> {
        return dbQuery.selectAllTokenPair(::mapTokens).executeAsList()
    }

    internal fun getImagesByBlog(uuid: String): List<ImagesEntity> {
        return dbQuery.selectAllImagesByBlog(uuid, mapper = ::mapImages).executeAsList()
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
}
