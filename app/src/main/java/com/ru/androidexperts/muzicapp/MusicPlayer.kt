package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.ru.androidexperts.muzicapp.presentation.mappers.Playlist


interface MusicPlayer {

    fun play(trackId: Long)

    fun pause()

    fun init(observableUpdate: (isLast: Boolean, trackId: Long) -> Unit)

    fun update(newPlayList: Playlist)

    class Base(context: Context) : MusicPlayer {

        private var playlistWasUpdated = false
        private var currentPlayList: Playlist = listOf()
        private var updateCallback = { _: Boolean, _: Long -> }

        private val listener = object : Listener {

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateCallback.invoke(IS_PLAYED, mediaItem?.mediaId?.toLong() ?: -1L)
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                updateCallback.invoke(
                    playWhenReady,
                    player.currentMediaItem?.mediaId?.toLong() ?: -1L
                )
                super.onPlayWhenReadyChanged(playWhenReady, reason)
            }
        }

        private val player = ExoPlayer.Builder(context)
            .build()

        override fun init(observableUpdate: (isLast: Boolean, index: Long) -> Unit) {
            updateCallback = observableUpdate
        }

        override fun update(newPlayList: Playlist) {
            currentPlayList = newPlayList
            playlistWasUpdated = true
        }

        override fun play(trackId: Long) {
            val trackIndex: Int = currentPlayList.map {
                it.first
            }.indexOf(trackId)
            if (playlistWasUpdated) {
                val mediaItems = currentPlayList.map {
                    MediaItem
                        .fromUri(it.second)
                        .buildUpon()
                        .setMediaId(it.first.toString())
                        .build()
                }
                player.setMediaItems(mediaItems)
                player.addListener(listener)
                player.prepare()
                player.seekTo(trackIndex, 0)
                playlistWasUpdated = false
            }
            if (player.currentMediaItemIndex != trackIndex)
                player.seekTo(trackIndex, 0)
            player.play()
        }

        override fun pause() {
            player.pause()
        }
    }

    companion object {
        const val IS_PLAYED = true
    }
}