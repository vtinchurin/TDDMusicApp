package com.ru.androidexperts.muzicapp.search.presentation

import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.core.player.PlayerCallback
import com.ru.androidexperts.muzicapp.di.ViewModelTag
import com.ru.androidexperts.muzicapp.search.domain.repository.SearchRepository
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchScreenActions
import com.ru.androidexperts.muzicapp.search.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.search.presentation.mappers.UiMapper
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist

class SearchViewModel(
    runAsync: RunAsync,
    observable: Playlist<SearchUiState>,
    private val repository: SearchRepository,
    private val player: MusicPlayer,
    private val toUi: UiMapper,
    private val toPlayList: PlayerMapper,
) : ViewModelTag.AbstractAsync<SearchUiState>(observable, runAsync), SearchScreenActions.Mutable {

    private val playerCallback = PlayerCallback { isPlaying, trackId ->
        if (isPlaying) {
            toUi.update(trackId)
            observable.play(trackId)
        } else {
            observable.stop()
            toUi.stop()
        }
    }

    fun init(isFirstRun: Boolean = true) {
        if (isFirstRun || processDeath) {
            processDeath = false
            player.init(playerCallback)
            val lastTerm = repository.lastCachedTerm()
            if (lastTerm.isNotEmpty()) {
                observable.updateUi(lastTerm)
                fetch(lastTerm)
            }
        }
    }

    fun fetch(term: String) {
        observable.updateUi(SearchUiState.Loading)
        handleAsync(
            heavyOperation = {
                val loadResult = repository.load(term)
                loadResult.map(toPlayList) to loadResult.map(toUi)
            }
        ) { (playlist, uiState) ->
            player.update(playlist)
            observable.updateUi(uiState)
        }
    }

    override fun retry() {
        fetch(repository.lastCachedTerm())
    }

    override fun play(trackId: Long) {
        player.play(trackId)
    }

    override fun pause() {
        player.pause()
    }
}