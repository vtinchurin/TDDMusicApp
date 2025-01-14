package com.ru.androidexperts.muzicapp.core.player

import androidx.media3.common.MediaItem
import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist


interface MusicPlayer {

    fun play(trackId: Long)

    fun pause()

    fun init(observableUpdate: PlayerCallback = PlayerCallback.Empty)

    fun update(newPlayList: Playlist)

    companion object {

        const val IS_PLAYED = true
    }

    fun MediaItem?.trackId(): Long = this?.mediaId?.toLong() ?: -1
}