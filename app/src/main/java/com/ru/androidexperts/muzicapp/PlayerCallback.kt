package com.ru.androidexperts.muzicapp

fun interface PlayerCallback {

    fun update(isPlayed: Boolean, trackId: Long)
    fun isEmpty() = false

    object Empty : PlayerCallback {
        override fun update(isPlayed: Boolean, trackId: Long) =
            throw IllegalStateException("You invoked empty callback")

        override fun isEmpty() = true
    }
}