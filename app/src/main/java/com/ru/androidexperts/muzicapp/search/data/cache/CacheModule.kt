package com.ru.androidexperts.muzicapp.search.data.cache

import android.content.Context
import androidx.room.Room
import com.ru.androidexperts.muzicapp.R

interface CacheModule {

    fun dao(): TracksDao

    class Base(applicationContext: Context) : CacheModule {

        private val database by lazy {
            Room.databaseBuilder(
                applicationContext,
                TracksDatabase::class.java,
                applicationContext.getString(R.string.app_name)
            ).build()
        }

        override fun dao() = database.dao()
    }
}