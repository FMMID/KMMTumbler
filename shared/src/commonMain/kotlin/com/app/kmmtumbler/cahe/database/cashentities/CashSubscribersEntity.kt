package com.app.kmmtumbler.cahe.database.cashentities

import com.app.kmmtumbler.cahe.database.entities.SubscribersEntity
import com.app.kmmtumbler.data.UserSubscriber
import com.app.kmmtumbler.utils.Cash
import com.app.kmmtumbler.utils.Result

data class CashSubscribersEntity(
    private val cashSubscribers: List<SubscribersEntity>
) :Cash {

    override fun mapToResult(): List<Result> {
        return cashSubscribers.map {
            UserSubscriber(
                name = it.name,
                url = it.url,
                updated = it.updated.toInt(),
                following = it.following
            )
        }
    }

    override fun getData(): List<Any> {
        return cashSubscribers
    }
}
