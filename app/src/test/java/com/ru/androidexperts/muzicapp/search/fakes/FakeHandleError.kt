package com.ru.androidexperts.muzicapp.search.fakes

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.search.data.DataException

interface FakeHandleError : HandleError {

    class Base : FakeHandleError {
        override fun handleError(e: Exception): DataException {
            return TestException()
        }
    }
}

class TestException : DataException(resId = -777)