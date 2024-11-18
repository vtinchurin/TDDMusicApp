package com.ru.androidexperts.muzicapp.presentation

import com.ru.androidexperts.muzicapp.MusicPlayer
import com.ru.androidexperts.muzicapp.SearchUiState
import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.domain.repository.SearchRepository
import com.ru.androidexperts.muzicapp.presentation.adapter.RecyclerActions
import com.ru.androidexperts.muzicapp.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.presentation.mappers.UiMapper
import com.ru.androidexperts.muzicapp.uiObservable.UiObservable
import com.ru.androidexperts.muzicapp.uiObservable.UiObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SearchViewModel(
    private val runAsync: RunAsync = RunAsync.Search(),
    private val observable: UiObservable.Playlist<SearchUiState> = UiObservable.Playlist.Base(),
    private val repository: SearchRepository,
    private val player: MusicPlayer,
    private val toUi: UiMapper,
    private val toPlayList: PlayerMapper,
) : RecyclerActions.Mutable {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init{
        player.init { isPlaying, trackId ->
            if (isPlaying) {
                toUi.update(trackId)
                observable.play(trackId)
            } else {
                observable.stop()
                toUi.update()
            }
        }
    }

    fun init(isFirstRun:Boolean = true) {
        if(isFirstRun){
            val lastTerm = repository.lastCachedTerm()
            if (lastTerm.isNotEmpty()){
                runAsync.handleAsync(viewModelScope, {
                    repository.load(lastTerm)
                }){ loadResult ->
                    player.update(loadResult.map(toPlayList))
                    observable.updateUi(SearchUiState.Initial(lastTerm,loadResult.map(toUi).recyclerState()))
                }
            } else observable.updateUi(SearchUiState.Initial())
        }
    }

    fun fetch(term: String) {
        if(term.isNotEmpty()) {
            observable.updateUi(SearchUiState.Loading)
            runAsync.handleAsync(viewModelScope, {
                repository.load(term)
            }) { loadResult ->
                player.update(loadResult.map(toPlayList))
                observable.updateUi(loadResult.map(toUi))
            }
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

    fun startUpdates(observer: UiObserver<SearchUiState>) {
        observable.update(observer)
    }

    fun stopUpdates() {
        observable.update()
    }
}