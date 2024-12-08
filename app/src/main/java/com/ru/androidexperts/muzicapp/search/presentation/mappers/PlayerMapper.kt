package com.ru.androidexperts.muzicapp.search.presentation.mappers

import com.ru.androidexperts.muzicapp.search.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.search.domain.model.TrackModel

typealias Playlist = List<Pair<Long, String>>
typealias Item = Pair<Long, String>

interface PlayerMapper : LoadResult.Mapper<Playlist> {

    class Base : PlayerMapper {

        override fun mapSuccess(data: List<TrackModel>): Playlist {
            return data.map {
                it.map(InnerMapper)
            }
        }

        override fun mapError(errorResId: Int): Playlist = emptyList()

        override fun mapNoTrack(): Playlist = emptyList()

        override fun mapEmpty(): Playlist = emptyList()

        private object InnerMapper : TrackModel.Mapper<Item> {

            override fun mapToTrackUi(
                id: Long,
                trackTitle: String,
                authorName: String,
                coverUrl: String,
                sourceUrl: String,
            ): Item {
                return id to sourceUrl
            }
        }
    }
}