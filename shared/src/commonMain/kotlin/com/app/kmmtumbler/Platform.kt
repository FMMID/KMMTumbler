package com.app.kmmtumbler

import com.squareup.sqldelight.db.SqlDriver

expect fun initLogger()

expect fun getUniqueState(): String

expect class DatabaseDriveFactory {
    fun createDriver(): SqlDriver
}