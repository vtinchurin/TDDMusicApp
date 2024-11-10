package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.domain.model.CustomException
import com.ru.androidexperts.muzicapp.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState

class UiMapper : TrackModel.Mapper<RecyclerItem> {

    override fun mapToTrackUi(
        id: Long,
        trackTitle: String,
        authorName: String,
        coverUrl: String,
        sourceUrl: String
    ): RecyclerItem {
        return RecyclerItem.TrackUi(
            trackId = id,
            coverUrl = coverUrl,
            authorName = authorName,
            trackTitle = trackTitle,
            isPlaying = PlayStopUiState.Stop
        )
    }

    override fun mapToNoTracks() = RecyclerItem.NoTracksUi

    override fun mapToError(e: CustomException): RecyclerItem {
        val resource = when (e) {
            is CustomException.NoInternetConnectionException -> e.toResource()
            else -> e.toResource()
        }
        return RecyclerItem.ErrorUi(resource)
    }
}