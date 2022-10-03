package com.app.kmmtumbler.di

import com.app.kmmtumbler.cahe.database.Database
import com.app.kmmtumbler.cahe.settings.TumblerSettings
import com.app.kmmtumbler.repositories.ITumblerRepository
import com.app.kmmtumbler.repositories.TumblerRepository
import com.app.kmmtumbler.models.TumblerViewModel
import com.app.kmmtumbler.network.api.authorization.ITumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.authorization.TumblerAuthorizationAPI
import com.app.kmmtumbler.network.api.user.ITumblerUserAPI
import com.app.kmmtumbler.network.api.user.TumblerUserAPI
import com.app.kmmtumbler.paging.PagingFollowingController
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val appModule = module {
    single { TumblerViewModel(get()) }
    single<ITumblerRepository> { TumblerRepository(get(), get(), get(), get()) }
    single { TumblerSettings() }
    single { Database(get()) }
    single<ITumblerAuthorizationAPI> { TumblerAuthorizationAPI() }
    single<ITumblerUserAPI> { TumblerUserAPI(get()) }
    single { (uuidBlog: String, clientScope: CoroutineScope) ->
        PagingFollowingController(uuidBlog, clientScope, get())
    }
}