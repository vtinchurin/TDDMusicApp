package com.ru.androidexperts.muzicapp.search.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist
import org.junit.Assert.assertEquals

interface FakeUiObservable<T : Any> : Playlist<T> {

    fun assertCurrentUiState(uiState: SearchUiState)

    fun assertUiStatesHistory(expectedUiStates: List<SearchUiState>)

    class Base(private val order: Order) : FakeUiObservable<SearchUiState> {

        private var cached: SearchUiState = SearchUiState.Initial()
        private var input = ""
        private var observer: UiObserver<SearchUiState> = UiObserver.Empty()
        private val uiStatesHistory = mutableListOf(cached)

        override fun updateUi(input: String) {
            this.input = input
            cached += input
        }

        override fun update(observer: UiObserver<SearchUiState>) {
            this.observer = observer
            if (observer.isNotEmpty()) {
                observer.updateUi(cached)
                order.add(OBSERVABLE_REGISTER)
                order.add(OBSERVABLE_POST)
            } else
                order.add(OBSERVABLE_UNREGISTER)
            input = ""
        }

        override fun updateUi(data: SearchUiState) {
            cached = data + input
            uiStatesHistory.add(cached)
            if (observer.isNotEmpty()) {
                observer.updateUi(cached)
                order.add(OBSERVABLE_POST)
            }
        }

        override fun play(trackId: Long) {
            updateUi(cached.play(trackId))
        }

        override fun stop() {
            updateUi(cached.stop())
        }

        override fun assertCurrentUiState(uiState: SearchUiState) {
            assertEquals(uiState, cached)
        }

        override fun assertUiStatesHistory(expectedUiStates: List<SearchUiState>) {
            assertEquals(expectedUiStates, uiStatesHistory)
        }
    }
}

const val OBSERVABLE_REGISTER = "observable register"
const val OBSERVABLE_UNREGISTER = "observable unregister"
const val OBSERVABLE_POST = "observable post"