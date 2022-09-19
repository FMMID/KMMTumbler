package com.app.kmmtumbler.cahe.database

import com.app.kmmtumbler.DatabaseDriveFactory
import com.app.kmmtumbler.cahe.database.entities.ImagesEntity
import com.app.kmmtumbler.cahe.database.entities.SubscribersEntity
import com.app.kmmtumbler.data.UserImage
import com.app.kmmtumbler.data.UserSubscriber
import com.app.kmmtumbler.shared.cache.TumblerDatabase

internal class Database(databaseDriveFactory: DatabaseDriveFactory) {

    private val database = TumblerDatabase(databaseDriveFactory.createDriver())
    private val dbQuery = database.tumblerDatabaseQueries

    internal fun getImagesByBlog(uuid: String): List<ImagesEntity> {
        return dbQuery.selectAllImagesByBlog(uuid, mapper = ::mapImages).executeAsList()
    }

    internal fun getSubscribersByBlog(uuid: String): List<SubscribersEntity> {
        return dbQuery.selectAllSubscribers(uuid, mapper = ::mapSubscribers).executeAsList()
    }

    internal fun insertImagesBlog(imagesEntity: ImagesEntity) {
        dbQuery.transaction {
            val list = dbQuery.selectAllImagesByBlog(imagesEntity.uuidBlog).executeAsList()
            if (list.find { it.imageUri == imagesEntity.uriImage } == null) {
                dbQuery.insertImagesBlog(imagesEntity.uuidBlog, imagesEntity.uriImage)
            }
        }
    }

    internal fun insertNewUserImages(uuidBlog: String, userImage: List<UserImage>) {
        userImage.map {
            insertImagesBlog(
                ImagesEntity(
                    uuidBlog = uuidBlog,
                    uriImage = it.uri
                )
            )
        }
    }

    internal fun insertNewUserSubscribers(uuidBlog: String, userSubscriber: List<UserSubscriber>) {
        userSubscriber.map {
            insertSubscribers(
                SubscribersEntity(
                    uuid = uuidBlog,
                    name = it.name,
                    url = it.url,
                    updated = it.updated.toLong(),
                    following = it.following
                )
            )
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
