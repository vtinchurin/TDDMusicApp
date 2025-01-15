package com.ru.androidexperts.muzicapp.core.adapter
/**
 *  [RecyclerActions.invoke] - is experimental method,
 *
 *  which is used to cast to descendant of [RecyclerActions]
 */
interface RecyclerActions {
    operator fun <T : RecyclerActions> invoke(): T = this as T
}