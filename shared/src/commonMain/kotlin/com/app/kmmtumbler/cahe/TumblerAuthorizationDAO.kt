package com.app.kmmtumbler.cahe

import com.app.kmmtumbler.cahe.entities.TokensEntity

class TumblerAuthorizationDAO(
    private val getActualTokensPairCallback: () -> TokensEntity?,
    private val insertNewTokensPairCallback: (String, String) -> Unit
) : ITumblerAuthorizationDAO {

    override fun getActualTokensPair(): TokensEntity? {
        return getActualTokensPairCallback.invoke()
    }

    override fun insertNewTokensPair(accessToken: String, refreshToken: String) {
        insertNewTokensPairCallback.invoke(accessToken, refreshToken)
    }
}