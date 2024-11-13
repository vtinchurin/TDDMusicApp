package com.ru.androidexperts.muzicapp.core

import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.data.NoInternetConnectionException
import com.ru.androidexperts.muzicapp.data.ServiceUnavailable
import java.io.IOException

interface HandleError {

    fun handleError(e: Exception): DataException

    class ToData : HandleError {
        override fun handleError(e: Exception): DataException {
            return when (e) {
                is IOException -> NoInternetConnectionException()
                else -> ServiceUnavailable()
            }
        }
    }
}