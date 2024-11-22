package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.uiObservable.UiObserver
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
