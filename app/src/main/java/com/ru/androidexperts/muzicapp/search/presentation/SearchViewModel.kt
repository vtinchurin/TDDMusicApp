package com.ru.androidexperts.muzicapp.search.presentation

import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.core.player.PlayerCallback
import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.di.ViewModelTag
import com.ru.androidexperts.muzicapp.search.domain.repository.SearchRepository
import com.ru.androidexperts.muzicapp.search.presentation.adapter.RecyclerActions
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
) : ViewModelTag.AbstractAsync<SearchUiState>(observable, runAsync), RecyclerActions.Mutable {

    private val playerCallback = PlayerCallback { isPlaying, trackId ->
        if (isPlaying) {
            toUi.update(trackId)
            observable.play(trackId)
        } else {
            observable.stop()
            toUi.update()
        }
    }

    fun init(isFirstRun: Boolean = true) {
        if (isFirstRun) {
            player.init(playerCallback)
            val lastTerm = repository.lastCachedTerm()
            observable.updateUi(lastTerm)
            if (lastTerm.isNotEmpty())
                fetch(lastTerm)
        }
    }

    fun fetch(term: String) {
        observable.updateUi(SearchUiState.Loading)
        handleAsync(
            heavyOperation = { repository.load(term) }
        ) { loadResult ->
            player.update(loadResult.map(toPlayList))
            observable.updateUi(loadResult.map(toUi))
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