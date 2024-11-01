package com.ru.androidexperts.muzicapp.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveTerm(value: TermsCache)

    @Query("select * from terms where term=:value")
    suspend fun term(value: String): TermsCache

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveTracks(tracks: List<TrackCache>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveTrackIdByTerm(trackIdByTermCache: TrackIdByTermCache)

    @Query("""
        SELECT
            tracks.id,
            tracks.track_title,
            tracks.author_name,
            tracks.cover_url,
            tracks.source_url
        FROM
            tracks
                INNER JOIN tracks_by_term
                    ON tracks.id = tracks_by_term.track_id
                INNER JOIN terms
                    ON tracks_by_term.term_id = terms.id
        WHERE
            terms.term=:term""")
    suspend fun tracksByTerm(term: String): List<TrackCache>
}