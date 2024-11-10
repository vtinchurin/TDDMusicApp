package com.ru.androidexperts.muzicapp.domain.model

import com.ru.androidexperts.muzicapp.R

interface CustomException {

    fun toResource(): Int

    abstract class Abstract(
        private val resId: Int,
    ) : Exception(), CustomException {
        override fun toResource() = resId
    }

    class NoInternetConnectionException : Abstract(R.string.no_internet_connection)

    class ServiceUnavailable : Abstract(R.string.service_unavailable)

}
