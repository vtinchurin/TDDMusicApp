package com.ru.androidexperts.muzicapp.search.viewModel.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist

interface FakeUiObservable<T : Any> : Playlist<T> {

    fun assertUpdateCalled(expectedObserver: UiObserver<SearchUiState>)
    fun assertUpdateUiCalled(expectedInput: String)
    fun assertUpdateUiCalled(expectedState: SearchUiState, isEmptyObserver: Boolean = false)
    fun assertPlayCalled(trackId: Long)
    fun assertStopCalled()

    data class Base(private val order: Order) : FakeUiObservable<SearchUiState> {

        private var cached: SearchUiState = SearchUiState.Initial()
        private var input = ""
        private var observer: UiObserver<SearchUiState> = UiObserver.Empty()

        override fun update(observer: UiObserver<SearchUiState>) {
            this.observer = observer
            if (observer.isNotEmpty()) {
                order.add(OBSERVABLE_REGISTER, observer)
                order.add(OBSERVABLE_POST) //TODO
                observer.updateUi(cached)
                input = ""
            } else
                order.add(OBSERVABLE_UNREGISTER, listOf(observer))
        }

        override fun assertUpdateCalled(expectedObserver: UiObserver<SearchUiState>) {
            if (expectedObserver.isNotEmpty()) {
                order.assert(OBSERVABLE_REGISTER, expectedObserver)
                order.assert(OBSERVABLE_POST)
            } else
                order.assert(OBSERVABLE_UNREGISTER, expectedObserver)
        }

        override fun assertUpdateUiCalled(expectedInput: String) {
            order.assert(OBSERVABLE_MAKE_INITIAL, expectedInput)
        }

        override fun assertUpdateUiCalled(expectedState: SearchUiState, isEmptyObserver: Boolean) {
            order.assert(OBSERVABLE_UPDATE_STATE, expectedState)
            if (!isEmptyObserver) {
                order.assert(OBSERVABLE_POST, expectedState)
            }
        }

        override fun assertPlayCalled(trackId: Long) {
            order.assert(OBSERVABLE_PLAY, trackId)
        }

        override fun assertStopCalled() {
            order.assert(OBSERVABLE_STOP)
        }

        override fun updateUi(input: String) {
            if (input.isNotEmpty()) {
                order.add(OBSERVABLE_MAKE_INITIAL, input)
                this.input = input
                cached += input
            }
        }

        override fun play(trackId: Long) {
            order.add(OBSERVABLE_PLAY, trackId)
            val nextState = cached.play(trackId)
            updateUi(nextState)
        }

        override fun stop() {
            order.add(OBSERVABLE_STOP)
            val nextState = cached.stop()
            updateUi(nextState)
        }

        override fun updateUi(data: SearchUiState) {
            cached = data + input
            order.add(OBSERVABLE_UPDATE_STATE, cached)
            if (observer.isNotEmpty()) {
                order.add(OBSERVABLE_POST, cached)
                observer.updateUi(cached)
            }
        }

    }

    companion object {
        private const val OBSERVABLE_REGISTER = "Observable#register"
        private const val OBSERVABLE_UNREGISTER = "Observable#unregister"
        private const val OBSERVABLE_POST = "Observable#post"
        private const val OBSERVABLE_UPDATE_STATE = "Observable#updateState"
        private const val OBSERVABLE_PLAY = "Observable#play"
        private const val OBSERVABLE_STOP = "Observable#stop"
        private const val OBSERVABLE_MAKE_INITIAL = "Observable#makeInitial"
    }
}
