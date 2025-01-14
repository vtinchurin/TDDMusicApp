package com.ru.androidexperts.muzicapp.di

import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.core.UiState
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface ViewModelTag {

    interface Observable<T : UiState> : ViewModelTag {

        fun startUpdates(observer: UiObserver<T>)

        fun stopUpdates()
    }

    abstract class Abstract<T : UiState>(
        protected val observable: Playlist<T>,
    ) : Observable<T> {

        protected var processDeath: Boolean = true

        override fun startUpdates(observer: UiObserver<T>) {
            observable.update(observer)
        }

        override fun stopUpdates() {
            observable.update(UiObserver.Empty())
        }
    }

    abstract class AbstractAsync<T : UiState>(
        observable: Playlist<T>,
        private val runAsync: RunAsync,
    ) : Abstract<T>(observable) {

        private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        protected fun <R : Any> handleAsync(
            heavyOperation: suspend () -> R,
            uiOperation: (R) -> Unit,
        ) {
            runAsync.handleAsync(viewModelScope, heavyOperation, uiOperation)
        }
    }
}