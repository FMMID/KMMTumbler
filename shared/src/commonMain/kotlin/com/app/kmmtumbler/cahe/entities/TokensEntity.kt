package com.app.kmmtumbler.cahe.entities

data class TokensEntity(
    val id: Long,
    val accessToken: String,
    val refreshToken: String
)