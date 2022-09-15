package com.app.kmmtumbler.cahe.entities

data class TokensEntity(
    val id: Long = 0,
    val accessToken: String,
    val refreshToken: String
)