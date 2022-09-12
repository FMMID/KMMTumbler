package com.app.kmmtumbler.utils

fun String.parseImage(): String? {
    val parsed = Regex("img src=.*jpg").find(this)?.value?.removePrefix("\\") ?: return null
    val parts = parsed.split("=")
    return if (parts.size > 1) {
        parts.last().removePrefix("\"")
    } else {
        null
    }
}