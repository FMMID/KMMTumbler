package com.app.kmmtumbler

import com.app.kmmtumbler.data.UserBlog
import com.app.kmmtumbler.di.appModule
import com.app.kmmtumbler.utils.AuthorizationStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

val iosModule = module {
    single { DatabaseDriveFactory() }
}

fun initKoin() {
    startKoin {
        modules(appModule + iosModule)
    }
}

class SDKTumblerHelper : KoinComponent, ISDKTumbler {
    private val sdkTumbler: ISDKTumbler by inject()

    override suspend fun authorization(): AuthorizationStatus {
        return sdkTumbler.authorization()
    }

    override suspend fun getTokenUser(code: String): Boolean {
        return sdkTumbler.getTokenUser(code)
    }

    override suspend fun getUserImages(): List<UserBlog> {
        return sdkTumbler.getUserImages()
    }
}