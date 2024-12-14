package com.ru.androidexperts.muzicapp.di

import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface ViewModelTag {

    interface Observable<T : Any> : ViewModelTag {

        fun startUpdates(observer: UiObserver<T>)

        fun stopUpdates()
    }

    abstract class Abstract<T : Any>(
        protected open val observable: Playlist<T>,
    ) : Observable<T> {

        protected var processDeath: Boolean = true

        override fun startUpdates(observer: UiObserver<T>) {
            observable.update(observer)
        }

        override fun stopUpdates() {
            observable.update()
        }
    }

    abstract class AbstractAsync<T : Any>(
        observable: Playlist<T>,
        protected open val runAsync: RunAsync,
    ) : Abstract<T>(observable) {

        protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        protected fun <R : Any> handleAsync(
            heavyOperation: suspend () -> R,
            uiOperation: (R) -> Unit,
        ) {
            runAsync.handleAsync(viewModelScope, heavyOperation, uiOperation)
        }
    }
}