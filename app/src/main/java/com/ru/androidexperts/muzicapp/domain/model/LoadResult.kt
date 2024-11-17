package com.ru.androidexperts.muzicapp.domain.model

interface LoadResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    data class Tracks(private val data: List<ResultEntityModel>) : LoadResult {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapSuccess(data)
        }
    }

    object Empty : LoadResult {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapEmpty()
        }
    }

    data class Error(private val error: ResultEntityModel) : LoadResult {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapError(error)
        }
    }


    interface Mapper<T : Any> {
        fun mapSuccess(data: List<ResultEntityModel>): T
        fun mapError(error: ResultEntityModel): T
        fun mapEmpty(): T
    }
}