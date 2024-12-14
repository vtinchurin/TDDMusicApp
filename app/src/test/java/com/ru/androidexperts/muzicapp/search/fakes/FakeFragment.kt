package com.ru.androidexperts.muzicapp.search.fakes

import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import org.junit.Assert.assertEquals

interface FakeFragment : UiObserver<SearchUiState> {

    fun assertCurrentUiState(expectedState: SearchUiState)

    fun assertUiStatesHistory(expectedUiStates: List<SearchUiState>)

    class Base : FakeFragment {

        private val statesList = mutableListOf<SearchUiState>()

        override fun updateUi(data: SearchUiState) {
            statesList.add(data)
        }

        override fun assertCurrentUiState(expectedState: SearchUiState) {
            assertEquals(expectedState, statesList.last())
        }

        override fun assertUiStatesHistory(expectedUiStates: List<SearchUiState>) {
            assertEquals(expectedUiStates, statesList)
        }
    }
}
