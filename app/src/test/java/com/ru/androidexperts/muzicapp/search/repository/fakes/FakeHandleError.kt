package com.ru.androidexperts.muzicapp.search.repository.fakes

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.data.DataException

interface FakeHandleError : HandleError {

    fun assertHandle()

    class Base(private val order: Order) : FakeHandleError {

        override fun assertHandle() {
            order.add(HANDLE_ERROR, TestException)
        }

        override fun handleError(e: Exception): DataException {
            return TestException
        }
    }

    companion object {
        private const val HANDLE_ERROR = "HandleError#handleError"
    }
}

data object TestException : DataException(resId = -777) {
    private fun readResolve(): Any = TestException
}