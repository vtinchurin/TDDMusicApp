package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.presentation.mappers.Playlist
import org.junit.Assert.assertEquals

interface FakeMusicPlayer : MusicPlayer{

    fun stopTracks()

    fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>)

    class Base(private val order: Order) : FakeMusicPlayer{

        private var currentTrack: Long = -1
        private var updateCallback = { _: Boolean, _: Long -> }
        private var uriList = listOf<Pair<Long, String>>()

        override fun stopTracks() {
            currentTrack = -1
            updateCallback.invoke(false, currentTrack)
        }

        override fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>) {
            assertEquals(tracksUriList, uriList)
        }

        override fun play(trackId: Long) {
            currentTrack = trackId
            updateCallback.invoke(true, trackId)
        }

        override fun pause() {
            updateCallback.invoke(false, -1)
        }

        override fun init(observableUpdate: (isLast: Boolean, trackId: Long) -> Unit) {
            updateCallback = observableUpdate
            order.add(PLAYER_INIT)
        }

        override fun update(newPlayList: Playlist) {
            uriList = newPlayList
            order.add(PLAYER_UPDATE)
        }
    }
}
const val PLAYER_UPDATE = "player update"
const val PLAYER_INIT = "player init"