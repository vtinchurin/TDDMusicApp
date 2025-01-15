package com.ru.androidexperts.muzicapp.search.presentation.uiObservable

import com.ru.androidexperts.muzicapp.core.uiObservable.UiObservable
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState

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
            cached += input
        }

        override fun update(observer: UiObserver<SearchUiState>) {
            this.observer = observer
            if (observer.isNotEmpty()) {
                observer.updateUi(cached)
                input = ""
            }
        }

        override fun updateUi(data: SearchUiState) {
            cached = data + input
            if (observer.isNotEmpty()) {
                observer.updateUi(cached)
            }
        }

        override fun play(trackId: Long) {
            updateUi(cached.play(trackId))
        }

        override fun stop() {
            updateUi(cached.stop())
        }
    }
}