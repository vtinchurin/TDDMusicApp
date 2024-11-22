package com.ru.androidexperts.muzicapp.data

import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.domain.model.LoadResult

abstract class DataException(private val resId: Int) : Exception() {

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(resId)
    }

    interface Mapper<T : Any> {

        fun map(resId: Int): T

        class ToErrorLoadResult : Mapper<LoadResult.Error> {
            override fun map(resId: Int): LoadResult.Error {
                return LoadResult.Error(resId)
            }
        }
    }

    class NoInternetConnectionException :
        DataException(R.string.no_internet_connection)

    class ServiceUnavailable :
        DataException(R.string.service_unavailable)

}