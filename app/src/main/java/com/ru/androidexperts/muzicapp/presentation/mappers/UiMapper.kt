package com.ru.androidexperts.muzicapp.presentation.mappers

import com.ru.androidexperts.muzicapp.SearchUiState
import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.ResultEntityModel
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState

interface UiMapper : LoadResult.Mapper<SearchUiState> {

    fun update(trackId: Long = -1L)

    class Base : UiMapper {

        private var playingTrackId = -1L

        override fun update(trackId: Long) {
            playingTrackId = trackId
        }

        override fun mapSuccess(data: List<ResultEntityModel>): SearchUiState {
            val mapper = InnerMapper()
            val tracksUiList = data.map {
                it.map(mapper)
            }
            return SearchUiState.Success(tracksUiList)
        }

        override fun mapError(error: ResultEntityModel): SearchUiState {
            val errorUi = error.map(InnerMapper())
            return SearchUiState.Error(errorUi)
        }

        override fun mapEmpty(): SearchUiState {
            return SearchUiState.NoTracks
        }

        private inner class InnerMapper :
            ResultEntityModel.Mapper<RecyclerItem> {

            override fun mapToTrackUi(
                id: Long,
                trackTitle: String,
                authorName: String,
                coverUrl: String,
                sourceUrl: String,
            ): RecyclerItem {
                return RecyclerItem.TrackUi(
                    trackId = id,
                    coverUrl = coverUrl,
                    authorName = authorName,
                    trackTitle = trackTitle,
                    isPlaying = if (this@Base.playingTrackId == id) PlayStopUiState.Play else PlayStopUiState.Stop
                )
            }

            override fun mapToNoTracks() = RecyclerItem.NoTracksUi

            override fun mapToError(resId: Int): RecyclerItem {
                return RecyclerItem.ErrorUi(resId = resId)
            }
        }
    }
}