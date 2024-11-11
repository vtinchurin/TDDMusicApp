package com.ru.androidexperts.muzicapp.data

import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.domain.model.DomainException

interface DataException {

    fun <T : Any> map(mapper: Mapper<T>): T

    abstract class Abstract(private val resId: Int) : Exception(), DataException {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.map(resId)
        }
    }

    class NoInternetConnectionException :
        Abstract(R.string.no_internet_connection)

    class ServiceUnavailable :
        Abstract(R.string.service_unavailable)

    interface Mapper<T : Any> {
        fun map(resId: Int): T

        class ToDomain : Mapper<DomainException> {
            override fun map(resId: Int): DomainException {
                return DomainException.Base(resId)
            }
        }
    }
}