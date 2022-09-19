package com.app.kmmtumbler.utils

fun String.parseImage(): String? {
    val imgReg = Regex("<img[^>]+\\bsrc=[\"']([^\"']+)[\"']")
    val imgContent = Regex("\".*\"")
    val parsed = imgReg.find(this)?.value ?: return null
    return imgContent.find(parsed)?.value?.removePrefix("\"")?.removeSuffix("\"")
}