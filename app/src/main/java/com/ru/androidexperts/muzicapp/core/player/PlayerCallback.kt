package com.ru.androidexperts.muzicapp.core.player

fun interface PlayerCallback {

    fun update(isPlayed: Boolean, trackId: Long)

    object Empty : PlayerCallback {
        override fun update(isPlayed: Boolean, trackId: Long) =
            throw IllegalStateException("You invoked empty callback")
    }
}