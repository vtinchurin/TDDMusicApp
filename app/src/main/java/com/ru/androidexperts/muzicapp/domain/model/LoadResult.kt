package com.ru.androidexperts.muzicapp.domain.model

interface LoadResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    data class Tracks(private val data: List<TrackModel>) : LoadResult {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapSuccess(data)
        }
    }

    object Empty: LoadResult {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapEmpty()
        }
    }

    object NoTracks:LoadResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapNoTrack()
        }
    }

    data class Error(private val errorResId: Int) : LoadResult {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapError(errorResId)
        }
    }


    interface Mapper<T : Any> {
        fun mapSuccess(data: List<TrackModel>): T
        fun mapError(errorResId: Int): T
        fun mapNoTrack():T
        fun mapEmpty(): T
    }
}