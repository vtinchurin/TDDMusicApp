package com.ru.androidexperts.muzicapp.view.trackImage

import com.ru.androidexperts.muzicapp.R
import com.squareup.picasso.Picasso
import java.io.Serializable

interface TrackImageUiState : Serializable {

    fun update(view: TrackImageUpdate)

    fun changeState(): TrackImageUiState

    abstract class Base(private val url: String) : TrackImageUiState {
        override fun update(view: TrackImageUpdate) {
            Picasso.get() //TODO need to looking for better solution for Picasso integration
                .load(url)
                .placeholder(R.drawable.ic_artwork)
                .error(R.drawable.ic_artwork)
                .into(view as TrackImage)
        }
    }

    data class Play(private val url: String) : Base(url) {
        override fun update(view: TrackImageUpdate) {
            super.update(view)
            view.startAnimation()
        }

        override fun changeState(): TrackImageUiState {
            return Stop(url)
        }
    }

    data class Stop(private val url: String) : Base(url) {
        override fun update(view: TrackImageUpdate) {
            super.update(view)
            view.stopAnimation()
        }

        override fun changeState(): TrackImageUiState {
            return Play(url)
        }
    }
}