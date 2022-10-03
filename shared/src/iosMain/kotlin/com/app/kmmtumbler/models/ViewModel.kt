package com.app.kmmtumbler.models

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

actual abstract class ViewModel {

    actual val viewModelScope = MainScope()

    protected actual open fun onCleared() = Unit

    fun clear() {
        onCleared()
        viewModelScope.cancel()
    }
}