package com.app.kmmtumbler.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class PagingFollowingControllerWrap(uuidBlog: String, clientScope: CoroutineScope? = null) : KoinComponent {

    val pagingFollowingController: PagingFollowingController by inject {
        parametersOf(
            uuidBlog,
            clientScope ?: MainScope()
        )
    }
}