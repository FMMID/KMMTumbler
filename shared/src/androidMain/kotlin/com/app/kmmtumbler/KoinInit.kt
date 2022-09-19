package com.app.kmmtumbler

import org.koin.dsl.module

val androidMode = module {
    single { DatabaseDriveFactory(get()) }
}