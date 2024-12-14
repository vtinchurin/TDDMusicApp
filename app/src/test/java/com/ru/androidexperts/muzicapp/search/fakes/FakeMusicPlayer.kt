package com.ru.androidexperts.muzicapp.search.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.core.player.PlayerCallback
import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist
import org.junit.Assert.assertEquals

interface FakeMusicPlayer : MusicPlayer {

    fun stopTracks()

    fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>)

    class Base(private val order: Order) : FakeMusicPlayer {

        private var currentTrack: Long = -1
        private var updateCallback: PlayerCallback = PlayerCallback.Empty
        private var uriList = listOf<Pair<Long, String>>()

        override fun stopTracks() {
            currentTrack = -1
            updateCallback.update(false, currentTrack)
        }

        override fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>) {
            assertEquals(tracksUriList, uriList)
        }

        override fun play(trackId: Long) {
            currentTrack = trackId
            updateCallback.update(true, trackId)
        }

        override fun pause() {
            updateCallback.update(false, -1)
        }

        override fun init(observableUpdate: PlayerCallback) {
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