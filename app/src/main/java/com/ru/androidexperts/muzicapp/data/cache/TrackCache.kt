package com.ru.androidexperts.muzicapp.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terms")
data class TermsCache(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("term")
    val term: String
)

@Entity(tableName = "tracks_by_term", primaryKeys = ["term_id", "track_id"])
data class TrackIdByTermCache(
    @ColumnInfo("term_id")
    val termId: Int,
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
