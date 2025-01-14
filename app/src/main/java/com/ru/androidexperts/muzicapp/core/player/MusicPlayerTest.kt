package com.ru.androidexperts.muzicapp.core.player

import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlayerTest : MusicPlayer {

    private var callback: PlayerCallback = PlayerCallback.Empty
    private var currentPlayList: Playlist = listOf()

    override fun init(observableUpdate: PlayerCallback) {
        callback = observableUpdate
    }

    override fun update(newPlayList: Playlist) {
        currentPlayList = newPlayList
    }

    override fun play(trackId: Long) {
        callback.update(MusicPlayer.IS_PLAYED, trackId)
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
            withContext(Dispatchers.IO) {
                delay(LONG_OPERATION_DELAY)
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
        callback.update(!MusicPlayer.IS_PLAYED, -1)
    }

    companion object {

        private const val LONG_OPERATION_DELAY = 3000L
    }
}