package com.ru.androidexperts.muzicapp

import org.junit.Assert.assertEquals

interface FakeFragment : UiObserver<SearchUiState> {

    fun assertCurrentUiStates(list: List<SearchUiState>)

    class Base : FakeFragment {

        private val statesList = mutableListOf<SearchUiState>()

        override fun updateUi(data: SearchUiState) {
            statesList.add(data)
        }

        override fun assertCurrentUiStates(list: List<SearchUiState>) {
            assertEquals(list, statesList)
        }
    }
}