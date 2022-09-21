package com.app.kmmtumbler.data

import com.app.kmmtumbler.utils.Result

data class UserSubscriber(
    val name: String,
    val url: String,
    val updated: Int,
    val following: Boolean
): Result