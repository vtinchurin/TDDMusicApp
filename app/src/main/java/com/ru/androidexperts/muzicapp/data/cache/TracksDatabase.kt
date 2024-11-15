package com.ru.androidexperts.muzicapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TermsCache::class, TrackIdByTermCache::class, TrackCache::class], version = 1)
abstract class TracksDatabase : RoomDatabase() {

    abstract fun dao(): TracksDao
}