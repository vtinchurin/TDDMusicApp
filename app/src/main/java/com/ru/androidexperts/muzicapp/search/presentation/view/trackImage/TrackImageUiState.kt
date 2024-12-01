package com.ru.androidexperts.muzicapp.search.presentation.view.trackImage

import com.ru.androidexperts.muzicapp.R
import com.squareup.picasso.Picasso
import java.io.Serializable

interface TrackImageUiState : Serializable {

    fun update(view: TrackImageUpdate)

    fun changeState(): TrackImageUiState

    data class Base(
        private val url: String,
        private val isPlaying: Boolean = false,
    ) : TrackImageUiState {
        override fun update(view: TrackImageUpdate) {
            Picasso.get() //TODO need to looking for better solution for Picasso integration
                .load(url)
                .placeholder(R.drawable.ic_artwork)
                .error(R.drawable.ic_artwork)
                .into(view as TrackImage)

            if (isPlaying)
                view.startAnimation()
            else
                view.stopAnimation()
        }

        override fun changeState(): TrackImageUiState {
            return this.copy(isPlaying = !isPlaying)
        }
    }
}