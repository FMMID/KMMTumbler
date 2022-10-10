package com.app.kmmtumbler.models

import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.paging.PagingFollowingControllerWrap
import com.app.kmmtumbler.repositories.ITumblerRepository
import com.app.kmmtumbler.utils.AuthorizationStatus
import io.github.aakira.napier.Napier

class TumblerViewModel(
    private val tumblerRepository: ITumblerRepository
) : ViewModel() {

    private var pagingFollowingControllerWrap: PagingFollowingControllerWrap? = null

    val pagingFollowingController
        get() = pagingFollowingControllerWrap?.pagingFollowingController

    override fun onCleared() {
        Napier.v("Clearing TumblerViewModel")
    }

    suspend fun authorization(): AuthorizationStatus {
        return tumblerRepository.authorization()
    }

    suspend fun getTokenUser(code: String): Boolean {
        return tumblerRepository.getTokenUser(code)
    }

    suspend fun getUserData(): List<UserBlog> {
        val data = tumblerRepository.getUserData()
        pagingFollowingControllerWrap = PagingFollowingControllerWrap(
            uuidBlog = data.last().uuidBlog
        )
        return data
    }
}