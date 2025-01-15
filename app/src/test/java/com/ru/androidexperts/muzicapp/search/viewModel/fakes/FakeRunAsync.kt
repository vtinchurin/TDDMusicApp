package com.ru.androidexperts.muzicapp.search.viewModel.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.RunAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

interface FakeRunAsync : RunAsync {

    fun returnResult()
    fun assertHandleAsyncCalled()

    class Base(private val order: Order) : FakeRunAsync {

        private lateinit var result: Any
        private var cached: (Any) -> Unit = {}

        override fun assertHandleAsyncCalled() {
            order.assert(RUN_ASYNC_HANDLE)
        }


        override fun returnResult() {
            cached.invoke(result)
        }

        override fun <T : Any> handleAsync(
            scope: CoroutineScope,
            heavyOperation: suspend () -> T,
            uiOperation: (T) -> Unit,
        ) {
            runBlocking {
                order.add(RUN_ASYNC_HANDLE)
                result = heavyOperation.invoke()
                cached = uiOperation as (Any) -> Unit
            }
        }

    }

    companion object {
        private const val RUN_ASYNC_HANDLE = "RunAsync#handleAsync"
    }
}