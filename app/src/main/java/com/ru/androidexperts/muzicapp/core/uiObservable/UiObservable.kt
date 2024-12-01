package com.ru.androidexperts.muzicapp.core.uiObservable

interface UiObservable<T : Any> : Update<T> {

    fun update(observer: UiObserver<T> = UiObserver.Empty())

}