package com.ru.androidexperts.muzicapp.presentation.mappers

import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.ResultEntityModel

typealias Playlist = List<Pair<Long, String>>
typealias Item = Pair<Long, String>

class PlayerMapper : LoadResult.Mapper<Playlist> {

    override fun mapSuccess(data: List<ResultEntityModel>): Playlist {
        return data.map {
            it.map(InnerMapper)
        }
    }

    override fun mapError(error: ResultEntityModel): Playlist {
        return listOf(error.map(InnerMapper))
    }

    override fun mapEmpty(): Playlist {
        return listOf()
    }

    private companion object InnerMapper : ResultEntityModel.Mapper<Item> {

        private val EMPTY = Item(-1L, "")

        override fun mapToTrackUi(
            id: Long,
            trackTitle: String,
            authorName: String,
            coverUrl: String,
            sourceUrl: String,
        ): Item {
            return id to sourceUrl
        }

        override fun mapToNoTracks(): Item {
            return EMPTY
        }

        override fun mapToError(resId: Int): Item {
            return EMPTY
        }
    }
}