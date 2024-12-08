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

    abstract class Abstract(
        private val delayMillis: Long = 0
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

    class Base : Abstract()

    class Search : Abstract(delayMillis = DELAY_BEFORE_HEAVY_OPERATION)

    companion object {

        private const val DELAY_BEFORE_HEAVY_OPERATION = 350L
    }
}