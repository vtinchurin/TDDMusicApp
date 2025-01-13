package com.ru.androidexperts.muzicapp.search.repository.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.cache.StringCache

interface FakeCache : StringCache {

    fun assertSave(expectedValue: String)
    fun assertRestore()

    data class Base(private val order: Order) : FakeCache {

        private var current = ""

        override fun assertSave(expectedValue: String) {
            order.assert(SHARED_PREFS_SAVE, expectedValue)
        }

        override fun assertRestore() {
            order.assert(SHARED_PREFS_RESTORE)
        }

        override fun save(value: String) {
            order.add(SHARED_PREFS_SAVE, value)
            current = value
        }

        override fun restore(): String {
            order.add(SHARED_PREFS_RESTORE)
            return current
        }

        companion object {
            private const val SHARED_PREFS_SAVE = "SharedPrefsCache#save"
            private const val SHARED_PREFS_RESTORE = "SharedPrefsCache#restore"
        }
    }
}