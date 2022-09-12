package com.app.kmmtumbler.cahe

import com.app.kmmtumbler.cahe.entities.TokensEntity

interface ITumblerAuthorizationDAO {

    fun getActualTokensPair(): TokensEntity?
}