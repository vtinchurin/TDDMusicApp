package com.ru.androidexperts.muzicapp.search.presentation.uiObservable

import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObservable
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver

interface Playlist<T : Any> : UiObservable<T> {

    fun updateUi(input: String)

    fun play(trackId: Long)

    fun stop()

    class Base : Playlist<SearchUiState> {

        private var cached: SearchUiState = SearchUiState.Initial()
        private var input = ""
        private var observer: UiObserver<SearchUiState> = UiObserver.Empty()

        override fun updateUi(input: String) {
            this.input = input
            cached = SearchUiState.Initial(input)
        }

        override fun update(observer: UiObserver<SearchUiState>) {
            this.observer = observer
            if (!observer.isEmpty()) {
                observer.updateUi(cached)
                input = ""
            }
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
    }
}