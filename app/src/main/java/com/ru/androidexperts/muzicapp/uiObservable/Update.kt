package com.ru.androidexperts.muzicapp.uiObservable

interface Update<T : Any> {

    fun updateUi(data: T)

}