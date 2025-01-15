package com.ru.androidexperts.muzicapp.search.uiObservable.fakes

import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState

interface FakeObserver : UiObserver<SearchUiState> {

    fun assertUpdateCalled(expectedState: SearchUiState)

    companion object {
        const val UPDATE_UI = "Observer#updateUi"
    }
}