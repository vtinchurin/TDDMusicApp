package com.ru.androidexperts.muzicapp.uiObservable

import com.ru.androidexperts.muzicapp.SearchUiState
import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState

interface UiObservable<T : Any> : Update<T> {

    fun update(observer: UiObserver<T> = UiObserver.Empty())

    interface Playlist<T : Any> : UiObservable<T> {

        fun play(track: RecyclerItem)

        fun stop()

        fun playNext()

        class Base : Playlist<SearchUiState> {

            private var cached: SearchUiState = SearchUiState.Initial("")
            private var observer: UiObserver<SearchUiState> = UiObserver.Empty()
            private var playedTrack: RecyclerItem = emptyTrack

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

            override fun play(track: RecyclerItem) {
                val tracks = cached.recyclerState().toMutableList()
                val playedTrackIndex = tracks.indexOf(track)
                playedTrack = track.changePlaying()
                tracks[playedTrackIndex] = playedTrack
                updateUi(SearchUiState.Success(recyclerState = tracks))
            }

            override fun stop() {
                val tracks = cached.recyclerState().toMutableList()
                if (tracks.contains(playedTrack)) {
                    val playedTrackIndex = tracks.indexOf(playedTrack)
                    tracks[playedTrackIndex] = playedTrack.changePlaying()
                    updateUi(SearchUiState.Success(recyclerState = tracks))
                }
                playedTrack = emptyTrack
            }

            override fun playNext() {
                val tracks = cached.recyclerState().toMutableList()
                if (tracks.contains(playedTrack)) {
                    val playedTrackIndex = tracks.indexOf(playedTrack)
                    tracks[playedTrackIndex] = playedTrack.changePlaying()
                    val nextTrack = tracks[playedTrackIndex + 1].changePlaying()
                    tracks[playedTrackIndex + 1] = nextTrack
                    updateUi(SearchUiState.Success(recyclerState = tracks))
                }
            }

            companion object {
                private val emptyTrack =
                    RecyclerItem.TrackUi(
                        -777L,
                        "https://dummydata.com",
                        "https://dummyimage.com",
                        "Empty",
                        "Empty",
                        PlayStopUiState.Stop
                    )
            }
        }
    }
}