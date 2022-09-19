package com.app.kmmtumbler.utils

fun <CASH : Cash, NETWORK : Network, RESULT : Result> getUserData(
    uuidBlog: String,
    cash: CASH,
    network: NETWORK?,
    saveDataCallback: (String, List<RESULT>) -> Unit
): List<RESULT> {
    return when {
        network != null && network.getData().size >= cash.getData().size -> {
            val result = network.mapToResult() as List<RESULT>
            saveDataCallback.invoke(uuidBlog, result)
            result
        }
        cash.getData().isNotEmpty() -> {
            cash.mapToResult() as List<RESULT>
        }
        else -> listOf()
    }
}

interface Result

interface Cash: UserData {
    override fun mapToResult(): List<Result>
    override fun getData(): List<Any>
}

interface Network: UserData {
    override fun mapToResult(): List<Result>
    override fun getData(): List<Any>
}

interface UserData {
    fun mapToResult(): List<Result>
    fun getData(): List<Any>
}