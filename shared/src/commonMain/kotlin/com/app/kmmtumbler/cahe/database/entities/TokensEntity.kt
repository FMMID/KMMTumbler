package com.app.kmmtumbler.cahe.database.entities

data class TokensEntity(
    val id: Long = 0,
    val accessToken: String,
    val refreshToken: String
)