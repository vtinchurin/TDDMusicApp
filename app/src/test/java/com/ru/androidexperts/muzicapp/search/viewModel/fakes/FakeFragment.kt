package com.ru.androidexperts.muzicapp.search.viewModel.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.uiObservable.fakes.FakeObserver

interface FakeFragment : FakeObserver {

    class Base(private val order: Order) : FakeFragment {

        override fun updateUi(data: SearchUiState) {
            order.add(UPDATE_UI, data)
        }

        override fun assertUpdateCalled(expectedState: SearchUiState) {
            order.assert(UPDATE_UI, expectedState)
        }
    }

    companion object {
        private const val UPDATE_UI = "Fragment#updateUi"
    }
}
