package com.app.kmmtumbler

import com.app.kmmtumbler.shared.cache.TumblerDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.Foundation.NSUUID

actual fun initLogger() {
    Napier.base(DebugAntilog())
}

actual fun getUniqueState(): String = NSUUID().UUIDString()

actual class DatabaseDriveFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TumblerDatabase.Schema, "test.db")
    }
}