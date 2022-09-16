package com.app.kmmtumbler.cahe.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class TumblerSettings {

    companion object {
        const val ACCESS_TOKEN_KEY = "access_token_key"
        const val REFRESH_TOKEN_KEY = "refresh_token_key"
    }

    private val settings: Settings = Settings()

    private var accessToken: String?
        get() = settings.getStringOrNull(ACCESS_TOKEN_KEY)
        set(value) {
            if (value != null) {
                settings[ACCESS_TOKEN_KEY] = value
            }
        }

    private var refreshToken: String?
        get() = settings.getStringOrNull(REFRESH_TOKEN_KEY)
        set(value) {
            if (value != null) {
                settings[REFRESH_TOKEN_KEY] = value
            }
        }

    fun getTokensPair(): TokensPair? {
        return TokensPair(
            accessToken = accessToken ?: return null,
            refreshToken = refreshToken ?: return null
        )
    }

    fun setTokensPair(tokensPair: TokensPair) {
        accessToken = tokensPair.accessToken
        refreshToken = tokensPair.refreshToken
    }
}

data class TokensPair(
    val accessToken: String,
    val refreshToken: String
)