package com.ru.androidexperts.muzicapp.core

import android.content.Context
import android.content.SharedPreferences


interface SharedPreferencesWrapper {

    fun sharedPreferences(context: Context): SharedPreferences

    abstract class Abstract(
        private val name: String,
        private val mode: Int = Context.MODE_PRIVATE,
    ) : SharedPreferencesWrapper {

        override fun sharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    data class Base(private val name: String) : Abstract(name = name)

    data object Test : Abstract(name = "test")
}