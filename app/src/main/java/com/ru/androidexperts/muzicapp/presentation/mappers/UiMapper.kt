package com.ru.androidexperts.muzicapp.presentation.mappers

import com.ru.androidexperts.muzicapp.SearchUiState
import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.view.trackImage.TrackImageUiState

interface UiMapper : LoadResult.Mapper<SearchUiState> {

    fun update(trackId: Long = -1L)

    class Base : UiMapper {

        private var playingTrackId = -1L

        override fun update(trackId: Long) {
            playingTrackId = trackId
        }

        override fun mapSuccess(data: List<TrackModel>): SearchUiState {
            val mapper = InnerMapper(playingTrackId)
            val tracksUiList = data.map {
                it.map(mapper)
            }
            return SearchUiState.Success(tracksUiList)
        }

        override fun mapError(errorResId: Int): SearchUiState {
            return SearchUiState.Error(errorResId)
        }

        override fun mapNoTrack(): SearchUiState {
            return SearchUiState.NoTracks
        }

        override fun mapEmpty(): SearchUiState {
            return SearchUiState.Initial()
        }

        private inner class InnerMapper(private val playingTrackId: Long = -1L) :
            TrackModel.Mapper<RecyclerItem> {

            override fun mapToTrackUi(
                id: Long,
                trackTitle: String,
                authorName: String,
                coverUrl: String,
                sourceUrl: String,
            ): RecyclerItem {
                return RecyclerItem.TrackUi(
                    trackId = id,
                    coverUrl = TrackImageUiState.Stop(coverUrl),
                    authorName = authorName,
                    trackTitle = trackTitle,
                    isPlaying = if (playingTrackId == id) PlayStopUiState.Play else PlayStopUiState.Stop
                )
            }
        }
    }
}