package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.Listener
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.exoplayer.ExoPlayer


interface MusicPlayer {

    fun play(trackId: Long)

    fun pause()

    fun update(tracksUri: List<Pair<Long, String>>, callback: (currentTrack: Long) -> Unit)

    class Base(context: Context) : MusicPlayer {

        private var playlistWasUpdated = false
        private var mediaItems: List<MediaItem> = listOf()
        private var updateCallback: (Long) -> Unit = {}
        private var currentTrackId: Long = -1
        private val listener = object : Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentTrackId = player.currentMediaItem?.mediaId?.toLong() ?: -1
                updateCallback.invoke(currentTrackId)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == STATE_ENDED) {
                    currentTrackId = -1
                    updateCallback.invoke(currentTrackId)
                } else if (playbackState == STATE_READY && player.playWhenReady)
                    updateCallback.invoke(currentTrackId)
            }
        }

        private val player = ExoPlayer.Builder(context)
            .build()
        init {
            player.addListener(listener)
        }

        override fun update(
            tracksUri: List<Pair<Long, String>>,
            callback: (currentTrackId: Long) -> Unit
        ) {
            val items = tracksUri.map { (id, uri) ->
                MediaItem.Builder().setMediaId(id.toString()).setUri(uri).build()
            }
            if (mediaItems != items) {
                mediaItems = items
                updateCallback = callback
                playlistWasUpdated = true
                updateCallback.invoke(currentTrackId)
            }
        }

        override fun play(trackId: Long) {
            if (playlistWasUpdated) {
                player.setMediaItems(mediaItems)
                player.prepare()
                playlistWasUpdated = false
            }
            mediaItems.forEachIndexed{ index, item->
                if (item.mediaId == trackId.toString()){
                    player.seekTo(
                        index,
                        if (trackId == currentTrackId) player.currentPosition else 0
                    )
                    player.play()
                }
            }
        }

        override fun pause() {
            player.pause()
            updateCallback.invoke(-1)
        }
    }
}
