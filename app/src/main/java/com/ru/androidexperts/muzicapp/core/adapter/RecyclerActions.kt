package com.ru.androidexperts.muzicapp.core.adapter

interface RecyclerActions {
    operator fun <T : RecyclerActions> invoke(): T = this as T
}