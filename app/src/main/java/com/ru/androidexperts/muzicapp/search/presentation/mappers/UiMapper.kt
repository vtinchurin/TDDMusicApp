package com.ru.androidexperts.muzicapp.search.presentation.mappers

import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.search.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.search.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState

interface UiMapper : LoadResult.Mapper<SearchUiState> {

    fun update(trackId: Long = MusicPlayer.EMPTY_TRACK_ID)

    class Base : UiMapper {

        private var playingTrackId = MusicPlayer.EMPTY_TRACK_ID

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

        override fun mapError(errorResId: Int): SearchUiState =
            SearchUiState.Error(errorResId)

        override fun mapNoTrack(): SearchUiState =
            SearchUiState.NoTracks

        override fun mapEmpty(): SearchUiState =
            SearchUiState.Initial()

        private inner class InnerMapper(
            private val playingTrackId: Long = MusicPlayer.EMPTY_TRACK_ID
        ) : TrackModel.Mapper<RecyclerItem> {

            override fun mapToTrackUi(
                id: Long,
                trackTitle: String,
                authorName: String,
                coverUrl: String,
                sourceUrl: String,
            ): RecyclerItem {
                return RecyclerItem.TrackUi(
                    trackId = id,
                    coverUrl = TrackImageUiState.Base(coverUrl,playingTrackId == id),
                    authorName = authorName,
                    trackTitle = trackTitle,
                    isPlaying = if (playingTrackId == id) PlayStopUiState.Play else PlayStopUiState.Stop
                )
            }
        }
    }
}