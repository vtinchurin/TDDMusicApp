package com.ru.androidexperts.muzicapp.core.uiObservable

fun interface UiObserver<T : Any> : Update<T> {

    fun isEmpty() = false

    class Empty<T : Any> : UiObserver<T> {

        override fun isEmpty() = true

        override fun updateUi(data: T) =
            throw IllegalArgumentException("Empty Observer was updated")
    }
}