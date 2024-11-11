package com.ru.androidexperts.muzicapp.domain.model

interface LoadResult {

    interface Success : LoadResult {
        fun <R : Any> map(mapper: TrackModel.Mapper<R>): List<R>
    }

    interface NoTracks : LoadResult {
        fun <R : Any> map(mapper: TrackModel.Mapper<R>): R
    }

    interface HandleError : LoadResult {
        fun <R : Any> map(mapper: TrackModel.Mapper<R>): R
    }

    class Tracks(private val data: List<TrackModel>) : Success {

        override fun <R : Any> map(mapper: TrackModel.Mapper<R>): List<R> {
            return data.map {
                it.map(mapper)
            }
        }
    }

    object Empty : NoTracks {
        override fun <R : Any> map(mapper: TrackModel.Mapper<R>): R {
            return mapper.mapToNoTracks()
        }
    }

    class Error(private val e: CustomException) : HandleError {
        override fun <R : Any> map(mapper: TrackModel.Mapper<R>): R {
            return mapper.mapToError(e.toResource())
        }
    }
}
