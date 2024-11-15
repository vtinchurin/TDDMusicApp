package com.ru.androidexperts.muzicapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {

    fun <T : Any> handleAsync(
        scope: CoroutineScope,
        heavyOperation: suspend () -> T,
        uiOperation: (T) -> Unit
    )

    class Base : RunAsync {
        override fun <T : Any> handleAsync(
            scope: CoroutineScope,
            heavyOperation: suspend () -> T,
            uiOperation: (T) -> Unit
        ) {
            scope.launch(Dispatchers.IO) {
                val result = heavyOperation.invoke()
                withContext(Dispatchers.Main) {
                    uiOperation.invoke(result)
                }
            }
        }
    }

    class Search(
        private val delayMillis: Long = 350L,
    ) : RunAsync {

        private var job: Job = Job()

        override fun <T : Any> handleAsync(
            scope: CoroutineScope,
            heavyOperation: suspend () -> T,
            uiOperation: (T) -> Unit
        ) {
            job.cancel()
            job = scope.launch(Dispatchers.IO) {
                delay(delayMillis)
                val result = heavyOperation.invoke()
                withContext(Dispatchers.Main) {
                    uiOperation.invoke(result)
                }
            }
        }
    }
}