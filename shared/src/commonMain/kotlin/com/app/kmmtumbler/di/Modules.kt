package com.app.kmmtumbler.di

import com.app.kmmtumbler.ISDKTumbler
import com.app.kmmtumbler.SDKTumbler
import org.koin.dsl.module

val appModule = module {
    single<ISDKTumbler> { SDKTumbler(get()) }
}