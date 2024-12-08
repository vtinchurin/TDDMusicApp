package com.ru.androidexperts.muzicapp.core

import com.ru.androidexperts.muzicapp.search.data.DataException
import java.io.IOException

interface HandleError {

    fun handleError(e: Exception): DataException

    class ToData : HandleError {
        override fun handleError(e: Exception): DataException {
            return if (e is IOException)
                DataException.NoInternetConnectionException()
            else
                DataException.ServiceUnavailable()
        }
    }
}