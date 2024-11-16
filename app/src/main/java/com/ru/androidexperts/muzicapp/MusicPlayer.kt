package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.ru.androidexperts.muzicapp.presentation.mappers.Playlist


interface MusicPlayer {

    fun play(trackId: Long)

    fun pause()

    fun update(
        newPlayList: List<Pair<Long, String>>,
        callback: (isLast: Boolean, index: Long) -> Unit,
    )

    class Base(context: Context) : MusicPlayer {

        private var playlistWasUpdated = false
        private var currentPlayList: Playlist = listOf()
        private var updateCallback = { _: Boolean, _: Long -> }
        private var isFirstUpdate = true

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

        override fun update(
            newPlayList: Playlist,
            callback: (isLast: Boolean, index: Long) -> Unit,
        ) {
            if (isFirstUpdate)
                updateCallback = callback
            isFirstUpdate = false
            newPlayList.find {
                it.first == (player.currentMediaItem?.mediaId?.toLong() ?: -1L)
            }?.let {
                updateCallback.invoke(IS_PLAYED, player.currentMediaItem?.mediaId?.toLong() ?: -1L)
            }
            currentPlayList = newPlayList
            playlistWasUpdated = true
        }

        override fun play(trackId: Long) {
            val trackIndex = currentPlayList.map {
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