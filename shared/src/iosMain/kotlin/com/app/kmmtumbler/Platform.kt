package com.app.kmmtumbler

import com.app.kmmtumbler.shared.cache.TumblerDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun initLogger() {
    Napier.base(DebugAntilog())
}

actual fun getUniqueState(): String = ""

actual class DatabaseDriveFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TumblerDatabase.Schema, "test.db")
    }
}