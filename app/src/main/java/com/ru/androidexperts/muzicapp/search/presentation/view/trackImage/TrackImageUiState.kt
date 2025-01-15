package com.ru.androidexperts.muzicapp.search.presentation.view.trackImage

import java.io.Serializable

interface TrackImageUiState : Serializable {

    fun update(view: TrackImageUpdate)

    fun changeState(): TrackImageUiState

    data class Base(
        private val url: String,
        private val isPlaying: Boolean = false,
    ) : TrackImageUiState {

        override fun update(view: TrackImageUpdate) {
            view.show(url)
            if (isPlaying)
                view.startAnimation()
            else
                view.stopAnimation()
        }

        override fun changeState(): TrackImageUiState =
            this.copy(isPlaying = !isPlaying)
    }
}