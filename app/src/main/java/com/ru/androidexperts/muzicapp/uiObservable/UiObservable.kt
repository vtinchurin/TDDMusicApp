package com.ru.androidexperts.muzicapp.uiObservable

import com.ru.androidexperts.muzicapp.SearchUiState

interface UiObservable<T : Any> : Update<T> {

    fun update(observer: UiObserver<T> = UiObserver.Empty())

    interface Playlist<T : Any> : UiObservable<T> {

        fun play(trackIndex: Int)

        fun stop()

        class Base : Playlist<SearchUiState> {

            private var cached: SearchUiState = SearchUiState.Initial("")
            private var observer: UiObserver<SearchUiState> = UiObserver.Empty()
            private var playedTrackIndex = -1

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

            //TODO refactor methods with scope functions
            override fun play(trackIndex: Int) {
                val tracks = cached.recyclerState().toMutableList()
                if (playedTrackIndex != -1) {
                    val playedTrack = tracks[playedTrackIndex].changePlaying()
                    tracks[playedTrackIndex] = playedTrack
                }
                playedTrackIndex = trackIndex
                val playedTrack = tracks[playedTrackIndex].changePlaying()
                tracks[playedTrackIndex] = playedTrack
                updateUi(SearchUiState.Success(recyclerState = tracks))
            }

            override fun stop() {
                val tracks = cached.recyclerState().toMutableList()
                val stoppedTrack = tracks[playedTrackIndex].changePlaying()
                tracks[playedTrackIndex] = stoppedTrack
                updateUi(SearchUiState.Success(recyclerState = tracks))
                playedTrackIndex = -1
            }

        }
    }
}