package com.ru.androidexperts.muzicapp.presentation.mappers

import com.ru.androidexperts.muzicapp.SearchUiState
import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.ResultEntityModel
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState

class UiMapper : LoadResult.Mapper<SearchUiState> {

    override fun mapSuccess(data: List<ResultEntityModel>): SearchUiState {
        val tracksUiList = data.map {
            it.map(InnerMapper)
        }
        return SearchUiState.Success(tracksUiList)
    }

    override fun mapError(error: ResultEntityModel): SearchUiState {
        val errorUi = error.map(InnerMapper)
        return SearchUiState.Error(errorUi)
    }

    override fun mapEmpty(): SearchUiState {
        return SearchUiState.NoTracks
    }

    companion object InnerMapper : ResultEntityModel.Mapper<RecyclerItem> {

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
                isPlaying = PlayStopUiState.Play
            )
        }

        override fun mapToNoTracks() = RecyclerItem.NoTracksUi

        override fun mapToError(resId: Int): RecyclerItem {
            return RecyclerItem.ErrorUi(resId = resId)
        }
    }
}