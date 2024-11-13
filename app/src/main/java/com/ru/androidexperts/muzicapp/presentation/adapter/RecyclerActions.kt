package com.ru.androidexperts.muzicapp.presentation.adapter

interface RecyclerActions {

    interface Retry {
        fun retry()
    }

    interface TogglePlayPause {
        fun play(trackId: Long)
        fun pause()
    }

    interface Mutable : Retry, TogglePlayPause
}