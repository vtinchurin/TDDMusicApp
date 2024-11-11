package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState

class UiMapper : TrackModel.Mapper<RecyclerItem> {

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
            isPlaying = PlayStopUiState.Stop
        )
    }

    override fun mapToNoTracks() = RecyclerItem.NoTracksUi

    override fun mapToError(resId: Int): RecyclerItem {
        return RecyclerItem.ErrorUi(resId = resId)
    }
}