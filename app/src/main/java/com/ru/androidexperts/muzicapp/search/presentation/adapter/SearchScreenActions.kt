package com.ru.androidexperts.muzicapp.search.presentation.adapter

import com.ru.androidexperts.muzicapp.core.adapter.RecyclerActions

interface SearchScreenActions : RecyclerActions {

    interface Retry : RecyclerActions {
        fun retry()
    }

    interface TogglePlayPause : RecyclerActions {
        fun play(trackId: Long)
        fun pause()
    }

    interface Mutable : Retry, TogglePlayPause
}