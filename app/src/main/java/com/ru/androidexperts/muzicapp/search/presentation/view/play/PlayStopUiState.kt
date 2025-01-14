package com.ru.androidexperts.muzicapp.search.presentation.view.play

import androidx.annotation.DrawableRes
import com.ru.androidexperts.muzicapp.R
import java.io.Serializable

interface PlayStopUiState : Serializable {

    fun update(updatePlayStop: UpdatePlayStopButton)
    fun isPlaying(): Boolean

    abstract class Abstract(@DrawableRes private val backgroundResId: Int) : PlayStopUiState {

        override fun update(updatePlayStop: UpdatePlayStopButton) {
            updatePlayStop.update(backgroundResId)
        }
    }

    object Play : Abstract(R.drawable.ic_stop) {
        override fun isPlaying() = true
    }

    object Stop : Abstract(R.drawable.ic_play) {
        override fun isPlaying() = false
    }
}