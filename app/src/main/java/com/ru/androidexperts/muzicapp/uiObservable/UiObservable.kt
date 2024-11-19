package com.ru.androidexperts.muzicapp.uiObservable

import com.ru.androidexperts.muzicapp.SearchUiState

interface UiObservable<T : Any> : Update<T> {

    fun update(observer: UiObserver<T> = UiObserver.Empty())

    interface Playlist<T : Any> : UiObservable<T> {

        fun update(input: String)

        fun play(trackId: Long)

        fun stop()

        class Base : Playlist<SearchUiState> {

            private var cached: SearchUiState = SearchUiState.Initial()
            private var input = ""
            private var observer: UiObserver<SearchUiState> = UiObserver.Empty()

            override fun update(input: String) {
                this.input = input
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
}