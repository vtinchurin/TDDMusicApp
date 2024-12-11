package com.ru.androidexperts.muzicapp.core.player

import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist


interface MusicPlayer {

    fun play(trackId: Long)

    fun pause()

    fun init(observableUpdate: PlayerCallback = PlayerCallback.Empty)

    fun update(newPlayList: Playlist)

    companion object {

        const val IS_PLAYED = true
        const val EMPTY_TRACK_ID = -1L
        const val SEEK_POSITION_MS = 0L
    }
}