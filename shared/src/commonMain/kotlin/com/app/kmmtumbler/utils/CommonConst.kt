package com.app.kmmtumbler.utils

object CommonConst {

    const val CLIENT_CONSUMER_KEY = "JPXGabRtZkIVfuaI0vBCXiFFoa4X6vsnFWHlpAxrtm1QYpShRB"
    const val CLIENT_SECRET_KEY = "OM589BCr8mP7Mn9coXFF2yqJlgNTpsLBUdjiOhRPSWFDMT97S4"
    const val REDIRECT_URI = "https://localhost/"
}

sealed class AuthorizationStatus(val value: String) {

    class Success(status: String) : AuthorizationStatus(status)
    class Failure(status: String) : AuthorizationStatus(status)
}
