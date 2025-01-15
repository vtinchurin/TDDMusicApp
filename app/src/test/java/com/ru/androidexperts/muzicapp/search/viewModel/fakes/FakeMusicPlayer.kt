package com.ru.androidexperts.muzicapp.search.viewModel.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.core.player.PlayerCallback
import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist

interface FakeMusicPlayer : MusicPlayer {

    fun stopTracks()
    fun assertInitCalled()
    fun assertUpdateCalled()
    fun assertPlayCalled(trackId: Long)
    fun assertPauseCalled()
    fun assertStopTracksCalled()

    data class Base(private val order: Order) : FakeMusicPlayer {

        private var updateCallback: PlayerCallback = PlayerCallback.Empty

        override fun stopTracks() {
            order.add(PLAYER_STOP)
            updateCallback.update(false, -1)
        }

        override fun assertInitCalled() {
            order.assert(PLAYER_INIT)
        }

        override fun assertUpdateCalled() {
            order.assert(PLAYER_UPDATE)
        }

        override fun assertPlayCalled(trackId: Long) {
            order.assert(PLAYER_PLAY, trackId)
        }

        override fun assertPauseCalled() {
            order.assert(PLAYER_PAUSE)
        }

        override fun assertStopTracksCalled() {
            order.assert(PLAYER_STOP)
        }

        override fun play(trackId: Long) {
            order.add(PLAYER_PLAY, trackId)
            updateCallback.update(true, trackId)
        }

        override fun pause() {
            order.add(PLAYER_PAUSE)
            updateCallback.update(false, -1)
        }

        override fun init(observableUpdate: PlayerCallback) {
            order.add(PLAYER_INIT)
            updateCallback = observableUpdate
        }

        override fun update(newPlayList: Playlist) {
            order.add(PLAYER_UPDATE)
        }
    }

    companion object {
        private const val PLAYER_UPDATE = "Player#update"
        private const val PLAYER_INIT = "Player#init"
        private const val PLAYER_PLAY = "Player#play"
        private const val PLAYER_PAUSE = "Player#pause"
        private const val PLAYER_STOP = "Player#stopTracks"
    }
}