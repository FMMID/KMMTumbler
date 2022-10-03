package com.app.kmmtumbler

import com.app.kmmtumbler.di.appModule
import com.app.kmmtumbler.models.TumblerViewModel
import org.koin.core.component.KoinComponent
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

@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent {
    fun getViewModel() = getKoin().get<TumblerViewModel>()
}
