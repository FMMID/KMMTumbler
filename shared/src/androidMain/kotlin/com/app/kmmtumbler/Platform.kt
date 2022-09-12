package com.app.kmmtumbler

import android.content.Context
import com.app.kmmtumbler.shared.cache.TumblerDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.UUID


actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun initLogger() {
    Napier.base(DebugAntilog())
}

actual fun getUniqueState(): String = UUID.randomUUID().toString()

actual class DatabaseDriveFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(TumblerDatabase.Schema, context, "test.db")
    }
}
