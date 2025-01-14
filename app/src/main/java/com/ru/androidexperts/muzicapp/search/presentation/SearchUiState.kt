package com.ru.androidexperts.muzicapp.search.presentation

import com.ru.androidexperts.muzicapp.core.UiState
import com.ru.androidexperts.muzicapp.core.adapter.GenericAdapter
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchItem
import com.ru.androidexperts.muzicapp.search.presentation.view.UpdateText

interface SearchUiState : UiState {

    fun show(
        input: UpdateText,
        adapter: GenericAdapter,
    )

    fun play(trackId: Long): SearchUiState {
        playedTrackId = trackId
        return this
    }

    fun stop(): SearchUiState {
        playedTrackId = -1L
        return this
    }

    operator fun plus(input: String): SearchUiState = this

    abstract class Abstract(
        private val recyclerState: List<SearchItem>,
    ) : SearchUiState {

        constructor(
            recyclerItem: SearchItem,
        ) : this(recyclerState = listOf(recyclerItem))

        override fun show(input: UpdateText, adapter: GenericAdapter) =
            adapter.update(recyclerState)

        override fun plus(input: String): SearchUiState =
            if (input.isNotEmpty()) {
                Initial(input, recyclerState)
            } else super.plus(input)

        fun <T : SearchItem> playTrack(trackId: Long): List<T> {
            val recyclerState = this.stopPlaying<SearchItem>() as MutableList
            playedTrackId = trackId
            recyclerState.filterIsInstance<SearchItem.Track>()
                .find { track ->
                    track.trackId() == trackId
                }?.let { track ->
                    val index = recyclerState.indexOf(track)
                    recyclerState[index] = track.changePlaying()
                }
            return recyclerState as List<T>
        }

        fun <T : SearchItem> stopPlaying(): List<T> {
            val recyclerState = recyclerState as MutableList
            recyclerState.filterIsInstance<SearchItem.Track>()
                .find { track ->
                    track.trackId() == playedTrackId
                }?.let { track ->
                    val index = recyclerState.indexOf(track)
                    recyclerState[index] = track.changePlaying()
                }
            playedTrackId = -1L
            return recyclerState as List<T>
        }
    }

    data class Initial(
        private val inputText: String = "",
        private val recyclerState: List<SearchItem> = listOf(),
    ) : Abstract(recyclerState) {

        constructor(
            inputText: String = "",
            recyclerState: SearchItem,
        ) : this(inputText, recyclerState = listOf(recyclerState))

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            if (inputText != "") input.update(inputText)
            super.show(input, adapter)
        }

        override fun play(trackId: Long): SearchUiState =
            this.copy(recyclerState = playTrack(trackId))

        override fun stop(): SearchUiState =
            this.copy(recyclerState = stopPlaying())
    }

    data class Success(
        private val recyclerState: List<SearchItem.Track>,
    ) : Abstract(recyclerState = recyclerState) {

        override fun play(trackId: Long): Success =
            this.copy(recyclerState = playTrack(trackId))

        override fun stop(): Success =
            this.copy(recyclerState = stopPlaying())
    }

    data class Error(private val errorResId: Int) :
        Abstract(recyclerItem = SearchItem.ErrorUi(resId = errorResId))

    data object Loading :
        Abstract(recyclerItem = SearchItem.ProgressUi)

    data object NoTracks :
        Abstract(recyclerItem = SearchItem.NoTracksUi)

    companion object {
        private var playedTrackId = -1L
    }
}