package com.ru.androidexperts.muzicapp

import org.junit.Assert.assertEquals

interface FakeMusicPlayer : MusicPlayer{
    fun stopTracks()
    fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>)

    class Base(private val order: Order) : FakeMusicPlayer{
        private var currentTrack: Long = -1
        private lateinit var updateCallback: (currentTrack: Long) -> Unit
        private var uriList = listOf<Pair<Long, String>>()

        override fun stopTracks() {
            currentTrack = -1
            updateCallback.invoke(currentTrack)
        }

        override fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>) {
            assertEquals(tracksUriList, uriList)
        }

        override fun play(trackId: Long) {
            currentTrack = trackId
            updateCallback.invoke(trackId)
        }

        override fun pause() {
            updateCallback.invoke(-1)
        }

        override fun update(
            tracksUri: List<Pair<Long, String>>,
            callback: (currentTrack: Long) -> Unit
        ) {
            order.add("player update")
            updateCallback = callback
            uriList = tracksUri
            updateCallback.invoke(currentTrack)
        }
    }
}
const val PLAYER_UPDATE = "player update"