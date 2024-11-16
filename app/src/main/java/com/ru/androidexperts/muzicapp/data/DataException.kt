package com.ru.androidexperts.muzicapp.data

import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.domain.model.ResultEntityModel

abstract class DataException(private val resId: Int) : Exception() {

    fun <T : Any> map(mapper: Mapper<T>): T {
        return mapper.map(resId)
    }

    interface Mapper<T : Any> {

        fun map(resId: Int): T

        class ToDomain : Mapper<ResultEntityModel> {
            override fun map(resId: Int): ResultEntityModel.Error {
                return ResultEntityModel.Error(resId)
            }
        }
    }

    class NoInternetConnectionException :
        DataException(R.string.no_internet_connection)

    class ServiceUnavailable :
        DataException(R.string.service_unavailable)

}