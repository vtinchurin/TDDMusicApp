package com.ru.androidexperts.muzicapp.domain.model

interface LoadResult {

    fun <R : Any> mapAll(mapper: TrackModel.Mapper<R>): List<R>

    class Tracks(private val data: List<TrackModel>) : LoadResult {

        override fun <R : Any> mapAll(mapper: TrackModel.Mapper<R>): List<R> {
            return data.map {
                it.map(mapper)
            }
        }
    }

    object Empty : LoadResult {
        override fun <R : Any> mapAll(mapper: TrackModel.Mapper<R>): List<R> {
            return listOf(mapper.mapToNoTracks())
        }
    }

    class Error(private val e: DomainException) : LoadResult {
        override fun <R : Any> mapAll(mapper: TrackModel.Mapper<R>): List<R> {
            return listOf(e.map(mapper))
        }
    }
}
