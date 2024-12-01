package com.ru.androidexperts.muzicapp.core.uiObservable

interface Update<T : Any> {

    fun updateUi(data: T)
}