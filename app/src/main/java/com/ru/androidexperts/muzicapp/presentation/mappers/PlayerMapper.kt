package com.ru.androidexperts.muzicapp.presentation.mappers

import com.ru.androidexperts.muzicapp.domain.model.TrackModel

typealias Playlist = Pair<Long, String>

class PlayerMapper : TrackModel.Mapper<Playlist> {
    override fun mapToTrackUi(
        id: Long,
        trackTitle: String,
        authorName: String,
        coverUrl: String,
        sourceUrl: String,
    ): Playlist {
        return id to sourceUrl
    }

    override fun mapToNoTracks(): Playlist {
        return EMPTY
    }

    override fun mapToError(resId: Int): Playlist {
        return EMPTY
    }

    companion object {
        private val EMPTY = Playlist(-1L, "")
    }
}