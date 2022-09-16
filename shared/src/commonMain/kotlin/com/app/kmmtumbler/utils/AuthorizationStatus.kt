package com.app.kmmtumbler.utils

sealed class AuthorizationStatus(val value: String) {

    class Success(status: String) : AuthorizationStatus(status)
    class Failure(status: String) : AuthorizationStatus(status)
}
