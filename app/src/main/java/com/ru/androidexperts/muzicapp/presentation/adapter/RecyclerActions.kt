package com.ru.androidexperts.muzicapp.presentation.adapter

interface RecyclerActions {

    interface Retry {
        fun retry()
    }

    interface TogglePlayPause {
        fun togglePlayPause(trackId: Long)
    }

    interface Mutable : Retry, TogglePlayPause
}