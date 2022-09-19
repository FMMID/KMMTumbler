package com.app.kmmtumbler.cahe.database.cashentities

import com.app.kmmtumbler.cahe.database.entities.ImagesEntity
import com.app.kmmtumbler.data.UserImage
import com.app.kmmtumbler.utils.Cash
import com.app.kmmtumbler.utils.Result

data class CashImagesEntity(
    private val cashImages: List<ImagesEntity>
) : Cash {

    override fun mapToResult(): List<Result> {
        return cashImages.map {
            UserImage(uri = it.uriImage)
        }
    }

    override fun getData(): List<Any> {
        return cashImages
    }
}
