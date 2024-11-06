package com.ru.androidexperts.muzicapp.uiObservable

import com.ru.androidexperts.muzicapp.SearchUiState

interface UiObservable<T : Any> : Update<T> {

    fun update(observer: UiObserver<T> = UiObserver.Empty())

    interface Playlist<T : Any> : UiObservable<T> {

        fun play(trackId: Long)

        fun stop()

        class Base : Playlist<SearchUiState> {

            private var cached: SearchUiState = SearchUiState.Initial("")
            private var observer: UiObserver<SearchUiState> = UiObserver.Empty()
            private var playedTrackId = -1L

            override fun update(observer: UiObserver<SearchUiState>) {
                this.observer = observer
                if (!observer.isEmpty())
                    observer.updateUi(cached)
            }

            override fun updateUi(data: SearchUiState) {
                if (!observer.isEmpty())
                    observer.updateUi(data)
                cached = data
            }

            override fun play(trackId: Long) {
                val tracks = cached.recyclerState().toMutableList()
                tracks.apply {
                    find {
                        it.trackId() == playedTrackId
                    }?.changePlaying()
                    find { targetTrack ->
                        targetTrack.trackId() == trackId
                    }?.let { targetTrack ->
                        targetTrack.changePlaying()
                        playedTrackId = trackId
                    }
                }.also { newState ->
                    updateUi(SearchUiState.Success(recyclerState = newState))
                }
            }

            override fun stop() {
                val tracks = cached.recyclerState().toMutableList()
                tracks.apply {
                    find {
                        it.trackId() == playedTrackId
                    }?.changePlaying()
                }.also { newState ->
                    playedTrackId = -1L
                    updateUi(SearchUiState.Success(recyclerState = newState))
                }
            }
        }
    }
}