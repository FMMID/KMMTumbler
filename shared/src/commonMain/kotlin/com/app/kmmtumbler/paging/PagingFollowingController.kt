package com.app.kmmtumbler.paging

import com.app.kmmtumbler.data.UserFollowing
import com.app.kmmtumbler.network.api.user.ITumblerUserAPI
import com.app.kmmtumbler.network.request.RequestUserFollowing
import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class PagingFollowingController(
    private val uuidBlog: String,
    private val clientScope: CoroutineScope,
    private val userAPI: ITumblerUserAPI
) {
    companion object {
        const val PAGE_LIMIT = 2
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pager = Pager<Int, UserFollowing>(
        clientScope = clientScope,
        config = PagingConfig(
            pageSize = PAGE_LIMIT,
            enablePlaceholders = false
        ),
        initialKey = 1,
        getItems = { currentKey, size ->
            var items = listOf<UserFollowing>()
            userAPI.getFollowing(
                uuidBlog, RequestUserFollowing(
                    limit = size,
                    offset = currentKey
                )
            ).onSuccess { result ->
                items = result.response.blogs.map {
                    UserFollowing(
                        name = it.name,
                        title = it.title,
                        description = it.description,
                        url = it.url,
                        uuid = it.uuid,
                        updated = it.updated
                    )
                }
            }
            Napier.v (tag ="PagigngFollowController",message="data received key:$currentKey")
            PagingResult(
                items = items,
                currentKey = currentKey,
                prevKey = {  null },
                nextKey = { currentKey + PAGE_LIMIT }
            )
        }
    )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagingDataAndroid: Flow<PagingData<UserFollowing>>
        get() = pager.pagingData.cachedIn(clientScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagingDataIOS: CommonFlow<PagingData<UserFollowing>>
        get() = pager.pagingData.cachedIn(clientScope).asCommonFlow()
}