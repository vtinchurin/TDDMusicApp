package com.ru.androidexperts.muzicapp.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveKey(key: String)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveTracks(tracks: List<TrackCache>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveTrackIdByKey(key: String, trackId: Long)

    @Query("select track_id from tracks_by_keys where search_key=:key")
    suspend fun trackIdsByKey(key: String): List<Long>

    @Query("select * from tracks where id=:id")
    suspend fun track(id: Long): TrackCache
}