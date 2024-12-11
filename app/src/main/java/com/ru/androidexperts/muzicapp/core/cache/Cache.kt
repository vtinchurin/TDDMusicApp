package com.ru.androidexperts.muzicapp.core.cache

interface Cache {

    interface Save<T : Any> {
        fun save(value: T)
    }

    interface Read<T : Any> {
        fun restore(): T
    }

    interface Mutable<T : Any> : Save<T>, Read<T>
}