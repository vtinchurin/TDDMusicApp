package com.ru.androidexperts.muzicapp.presentation.mappers

import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.TrackModel

typealias Playlist = List<Pair<Long, String>>
typealias Item = Pair<Long, String>

interface PlayerMapper : LoadResult.Mapper<Playlist> {

    class Base : PlayerMapper {

        override fun mapSuccess(data: List<TrackModel>): Playlist {
            return data.map {
                it.map(InnerMapper)
            }
        }

        override fun mapError(errorResId: Int): Playlist {
            return listOf()
        }

        override fun mapNoTrack(): Playlist {
            return listOf()
        }

        override fun mapEmpty(): Playlist {
            return listOf()
        }

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