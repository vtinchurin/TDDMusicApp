package com.ru.androidexperts.muzicapp.core.cache

import android.content.SharedPreferences

interface StringCache : Cache.Mutable<String> {

    class Base(
        private val key: String,
        private val sharedPreferences: SharedPreferences,
        private val defaultValue: String,
    ) : StringCache {

        override fun save(value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        override fun restore(): String {
            return sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }
    }
}