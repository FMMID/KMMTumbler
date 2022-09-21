package com.app.kmmtumbler.di

import com.app.kmmtumbler.ISDKTumbler
import com.app.kmmtumbler.SDKTumbler
import com.app.kmmtumbler.cahe.settings.TumblerSettings
import com.app.kmmtumbler.network.api.user.ITumblerUserAPI
import com.app.kmmtumbler.network.api.user.TumblerUserAPI
import com.app.kmmtumbler.paging.PagingFollowingController
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val appModule = module {
    single<ISDKTumbler> { SDKTumbler(get()) }
    single { TumblerSettings() }
    single<ITumblerUserAPI> { TumblerUserAPI(get()) }
    single { (uuidBlog: String, clientScope: CoroutineScope) ->
        PagingFollowingController(uuidBlog, clientScope, get())
    }
}