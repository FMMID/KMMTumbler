plugins {
    id("base-configuration")
}

sqldelight {
    database("TumblerDatabase") {
        packageName = "com.app.kmmtumbler.shared.cache"
        sourceFolders = listOf("sqldelight")
    }
}

//allows you to create config variable
buildkonfig {
    packageName = "com.app.kmmtumbler"
    objectName = "TumblerPublicConfig"
    exposeObjectWithName = "TumblerPublicConfig"
}