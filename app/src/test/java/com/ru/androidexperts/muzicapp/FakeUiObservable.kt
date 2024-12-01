package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist
import org.junit.Assert.assertEquals

interface FakeUiObservable<T: Any> : Playlist<T> {

    fun assertCurrentUiState(uiState: SearchUiState)

    fun assertUiStatesHistory(expectedUiStates: List<SearchUiState>)

    class Base(private val order: Order) : FakeUiObservable<SearchUiState> {

        private var cached: SearchUiState = SearchUiState.Initial("")
        private var input = ""
        private var observer: UiObserver<SearchUiState> = UiObserver.Empty()
        private val uiStatesHistory = mutableListOf<SearchUiState>()

        override fun updateUi(input: String) {
            this.input = input
        }

        override fun update(observer: UiObserver<SearchUiState>) {
            this.observer = observer
            if (!observer.isEmpty()) {
                observer.updateUi(cached)
                order.add(OBSERVABLE_REGISTER)
                order.add(OBSERVABLE_POST)
                uiStatesHistory.add(cached)
            } else
                order.add(OBSERVABLE_UNREGISTER)
            input = ""
        }

        override fun updateUi(data: SearchUiState) {
            cached = if (input.isNotEmpty()) {
                SearchUiState.Initial(input, data.recyclerState())
            } else
                data

            if (!observer.isEmpty()) {
                observer.updateUi(cached)
                order.add(OBSERVABLE_POST)
                uiStatesHistory.add(cached)
            }
        }

        override fun play(trackId: Long) {
            val tracks = cached.recyclerState().toMutableList()
            tracks.find {
                it.isPlaying()
            }?.let {
                val index = tracks.indexOf(it)
                tracks[index] = it.changePlaying()
            }
            tracks.find {
                it.trackId() == trackId
            }?.let {
                val index = tracks.indexOf(it)
                tracks[index] = it.changePlaying()
            }
            updateUi(SearchUiState.Success(recyclerState = tracks))
        }

        override fun stop() {
            val tracks = cached.recyclerState().toMutableList()
            tracks.find {
                it.isPlaying()
            }?.let {
                val index = tracks.indexOf(it)
                tracks[index] = it.changePlaying()
            }
            updateUi(SearchUiState.Success(recyclerState = tracks))
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
const val OBSERVABLE_POST= "observable post"