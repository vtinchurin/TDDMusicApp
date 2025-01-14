package com.ru.androidexperts.muzicapp.core.player

import com.ru.androidexperts.muzicapp.search.presentation.mappers.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlayerTest : MusicPlayer {

    private var callback: PlayerCallback = PlayerCallback.Empty
    private var currentPlayList: Playlist = listOf()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var playlistSize = 0
    private var job: Job = Job()

    override fun init(observableUpdate: PlayerCallback) {
        callback = observableUpdate
    }

    override fun update(newPlayList: Playlist) {
        currentPlayList = newPlayList
        playlistSize = newPlayList.size
    }

    override fun play(trackId: Long) {
        job.cancel()
        callback.update(MusicPlayer.IS_PLAYED, trackId)
        val trackIndex = currentPlayList.indexOf(
            currentPlayList.find { it.first == trackId }
        )
        job = scope.launch {
            withContext(Dispatchers.IO) {
                delay(LONG_OPERATION_DELAY)
            }
            withContext(Dispatchers.Main) {
                pause()
                if (trackIndex != currentPlayList.lastIndex)
                    play(currentPlayList[trackIndex + 1].first)
            }
        }
    }

    override fun pause() {
        callback.update(!MusicPlayer.IS_PLAYED, MusicPlayer.EMPTY_TRACK_ID)
    }

    companion object {

        private const val LONG_OPERATION_DELAY = 3000L
    }
}