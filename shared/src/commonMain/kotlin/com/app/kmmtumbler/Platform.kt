package com.app.kmmtumbler

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.*

expect fun initLogger()

expect fun getUniqueState(): String

expect class DatabaseDriveFactory {
    fun createDriver(): SqlDriver
}