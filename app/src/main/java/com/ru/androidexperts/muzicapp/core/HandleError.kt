package com.ru.androidexperts.muzicapp.core

import com.ru.androidexperts.muzicapp.data.DataException
import java.io.IOException

interface HandleError {

    fun handleError(e: Exception): DataException

    class ToData : HandleError {
        override fun handleError(e: Exception): DataException {
            return when (e) {
                is IOException -> DataException.NoInternetConnectionException()
                else -> DataException.ServiceUnavailable()
            }
        }
    }
}