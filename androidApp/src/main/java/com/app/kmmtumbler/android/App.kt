package com.app.kmmtumbler.android

import android.app.Application
import com.app.kmmtumbler.androidMode
import com.app.kmmtumbler.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(appModule + androidMode)
        }
    }
}