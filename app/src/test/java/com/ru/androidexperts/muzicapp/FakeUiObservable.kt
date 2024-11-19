package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.uiObservable.UiObservable
import com.ru.androidexperts.muzicapp.uiObservable.UiObserver
import org.junit.Assert.assertEquals

interface FakeUiObservable<T: Any> : UiObservable.Playlist<T> {

    fun assertUiStates(uiState: SearchUiState)

    class Base(
        private val order: Order
    ) : FakeUiObservable<SearchUiState> {

        private var cached: SearchUiState = SearchUiState.Initial("")
        private var input = ""
        private var observer: UiObserver<SearchUiState> = UiObserver.Empty()

        override fun update(input: String) {
            this.input = input
        }

        override fun update(observer: UiObserver<SearchUiState>) {
            this.observer = observer
            if (!observer.isEmpty())
                observer.updateUi(cached)
            input = ""
            order.add(OBSERVABLE_REGISTER)
        }

        override fun updateUi(data: SearchUiState) {
            cached = if (input.isNotEmpty()) {
                SearchUiState.Initial(input, data.recyclerState())
            } else data
            if (!observer.isEmpty()) {
                observer.updateUi(cached)
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

        override fun assertUiStates(uiState: SearchUiState) {
            assertEquals(uiState, cached)
        }
    }
}

const val OBSERVABLE_REGISTER = "observable register"
const val OBSERVABLE_UNREGISTER = "observable unregister"
const val OBSERVABLE_POST= "observable post"