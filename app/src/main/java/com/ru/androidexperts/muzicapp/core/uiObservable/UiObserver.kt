package com.ru.androidexperts.muzicapp.core.uiObservable

fun interface UiObserver<T : Any> : Update<T> {

    fun isNotEmpty() = true

    class Empty<T : Any> : UiObserver<T> {

        override fun isNotEmpty() = false

        override fun updateUi(data: T) =
            throw IllegalArgumentException("Empty Observer was updated")
    }
}