package com.ru.androidexperts.muzicapp

import org.junit.Assert.assertEquals

interface FakeMusicPlayer : MusicPlayer {
    fun stopTracks()
    fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>)

    class Base(private val order: Order) : FakeMusicPlayer {
        private var currentTrack: Long = -1
        private lateinit var updateCallback: (isLast: Boolean, trackId: Long) -> Unit
        private var uriList = listOf<Pair<Long, String>>()

        override fun stopTracks() {
            currentTrack = -1
            updateCallback.invoke(
                true,
                currentTrack
            )
        }

        override fun assertUpdateTracksUriList(tracksUriList: List<Pair<Long, String>>) {
            assertEquals(tracksUriList, uriList)
        }

        override fun play(trackId: Long) {
            currentTrack = trackId
            updateCallback.invoke(
                currentTrack == -1L,
                trackId
            )
        }

        override fun pause() {
            currentTrack = -1L
            updateCallback.invoke(true, -1)
        }

        override fun init(observableUpdate: (isLast: Boolean, trackId: Long) -> Unit) {
            updateCallback = observableUpdate
        }

        override fun update(
            tracksUri: List<Pair<Long, String>>
        ) {
            order.add("player update")
            uriList = tracksUri
            updateCallback.invoke(
                currentTrack == -1L,
                currentTrack
            )
        }
    }
}

const val PLAYER_UPDATE = "player update"