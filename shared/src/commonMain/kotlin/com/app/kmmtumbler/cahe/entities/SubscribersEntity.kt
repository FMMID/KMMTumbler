package com.app.kmmtumbler.cahe.entities

data class SubscribersEntity(
    val id: Long = 0,
    val uuid:String,
    val name: String,
    val url: String,
    val updated: Long,
    val following: Boolean
)