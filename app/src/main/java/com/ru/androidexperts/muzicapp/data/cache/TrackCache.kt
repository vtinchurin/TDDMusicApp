package com.ru.androidexperts.muzicapp.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_keys")
data class SearchKeysCache(
    @PrimaryKey
    @ColumnInfo("key")
    val key: String
)

@Entity(tableName = "tracks_by_keys", primaryKeys = ["search_key", "track_id"])
data class TrackIdByKeysCache(
    @ColumnInfo("search_key")
    val searchKey: String,
    @ColumnInfo("track_id")
    val trackId: Long
)

@Entity(tableName = "tracks")
data class TrackCache(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("track_title")
    val trackTitle: String,
    @ColumnInfo("author_name")
    val authorName: String,
    @ColumnInfo("cover_url")
    val coverUrl: String,
    @ColumnInfo("source_url")
    val sourceUrl: String
)
