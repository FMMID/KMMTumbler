package com.app.kmmtumbler.cahe

import com.app.kmmtumbler.cahe.entities.TokensEntity

class TumblerAuthorizationDAO(
    private val getActualTokensPairCallback: () -> TokensEntity?
) : ITumblerAuthorizationDAO {

    override fun getActualTokensPair(): TokensEntity? {
        return getActualTokensPairCallback.invoke()
    }
}