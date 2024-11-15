package com.ru.androidexperts.muzicapp.view.play

import androidx.annotation.DrawableRes
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.view.UiState

interface PlayStopUiState : UiState {

    fun update(updatePlayStop: UpdatePlayStopButton)

    abstract class Abstract(@DrawableRes private val backgroundResId: Int) : PlayStopUiState {

        override fun update(updatePlayStop: UpdatePlayStopButton) {
            updatePlayStop.update(backgroundResId)
        }
    }

    object Play : Abstract(R.drawable.ic_stop)
    object Stop : Abstract(R.drawable.ic_play)
}