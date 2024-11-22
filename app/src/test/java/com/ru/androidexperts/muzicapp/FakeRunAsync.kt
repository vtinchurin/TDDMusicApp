package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.core.RunAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

interface FakeRunAsync : RunAsync {

    fun returnResult()

    class Base(private val order: Order) : FakeRunAsync {

        private lateinit var result: Any
        private var cached: (Any) -> Unit = {}

        override fun <T : Any> handleAsync(
            scope: CoroutineScope,
            heavyOperation: suspend () -> T,
            updateUi: (T) -> Unit
        ) {
            runBlocking {
                order.add(RUN_ASYNC_HANDLE)
                result = heavyOperation.invoke()
                cached = updateUi as (Any) -> Unit
            }
        }

        override fun returnResult() {
            cached.invoke(result)
            order.add(RUN_ASYNC_RETURN_RESULT)
        }
    }
}

const val RUN_ASYNC_HANDLE = "runAsync handle"
const val RUN_ASYNC_RETURN_RESULT = "runAsync returnResult"