package com.ru.androidexperts.muzicapp

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.ru.androidexperts.muzicapp.presentation.mappers.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface MusicPlayer {

    fun play(trackId: Long)

    fun pause()

    fun init(observableUpdate: PlayerCallback = PlayerCallback.Empty)

    fun update(newPlayList: Playlist)

    class Base(private val player: ExoPlayer) : MusicPlayer {

        private var playlistWasUpdated = false
        private var currentPlayList: Playlist = listOf()
        private var updateCallback: PlayerCallback = PlayerCallback.Empty

        private val listener = object : Listener {

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateCallback.update(IS_PLAYED, mediaItem?.mediaId?.toLong() ?: -1L)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if(player.playbackState == Player.EVENT_IS_LOADING_CHANGED){
                    updateCallback.update(
                        isPlaying,
                        player.currentMediaItem?.mediaId?.toLong() ?: -1L
                    )
                }
                if (player.playbackState == Player.EVENT_PLAYBACK_STATE_CHANGED) {
                    updateCallback.update(
                        !IS_PLAYED,
                        player.currentMediaItem?.mediaId?.toLong() ?: -1L
                    )
                }
                super.onIsPlayingChanged(isPlaying)
            }
        }

        override fun init(observableUpdate: PlayerCallback) {
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
            else if(player.playbackState == Player.EVENT_PLAYBACK_STATE_CHANGED)
                player.seekTo(0)
            player.play()
        }

        override fun pause() {
            player.pause()
        }
    }

    class TestPlayer : MusicPlayer {

        private var callback: PlayerCallback = PlayerCallback.Empty
        private var currentPlayList: Playlist = listOf()

        override fun init(observableUpdate: PlayerCallback) {
            callback = observableUpdate
        }

        override fun update(newPlayList: Playlist){
            currentPlayList = newPlayList
        }

        override fun play(trackId: Long) {
            callback.update(IS_PLAYED,trackId)
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
                withContext(Dispatchers.IO) {
                    delay(3000)
                }
                withContext(Dispatchers.Main) {
                    pause()

                    val indexOfNextTrack = currentPlayList.indexOf(
                        currentPlayList.find { it.first == trackId }
                    ) + 1
                    if (indexOfNextTrack <= currentPlayList.lastIndex)
                        play(currentPlayList[indexOfNextTrack].first)
                }
            }
        }

        override fun pause() {
            callback.update(!IS_PLAYED,-1)
        }
    }

    companion object {
        const val IS_PLAYED = true
    }
}