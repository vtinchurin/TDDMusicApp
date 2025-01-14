package com.ru.androidexperts.muzicapp.core.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist

class MusicPlayerBase(private val player: ExoPlayer) : MusicPlayer {

    private var playlistWasUpdated = false
    private var currentPlayList: Playlist = listOf()
    private var updateCallback: PlayerCallback = PlayerCallback.Empty

    private val listener = object : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateCallback.update(
                MusicPlayer.IS_PLAYED,
                mediaItem.trackId()
            )
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (player.playbackState == Player.EVENT_IS_LOADING_CHANGED) {
                updateCallback.update(
                    isPlaying,
                    player.currentMediaItem.trackId()
                )
            }
            if (player.playbackState == Player.EVENT_PLAYBACK_STATE_CHANGED) {
                updateCallback.update(
                    !MusicPlayer.IS_PLAYED,
                    player.currentMediaItem.trackId()
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
                MediaItem.fromUri(it.second)
                    .buildUpon()
                    .setMediaId(it.first.toString())
                    .build()
            }
            player.setMediaItems(mediaItems)
            player.addListener(listener)
            player.prepare()
            player.seekTo(trackIndex, SEEK_POSITION_MS)
            playlistWasUpdated = false
        }
        if (player.currentMediaItemIndex != trackIndex)
            player.seekTo(trackIndex, SEEK_POSITION_MS)
        else if (player.playbackState == Player.EVENT_PLAYBACK_STATE_CHANGED)
            player.seekTo(SEEK_POSITION_MS)
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    companion object {
        const val SEEK_POSITION_MS = 0L
    }
}
