package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ru.androidexperts.muzicapp.data.cache.TermsCache
import com.ru.androidexperts.muzicapp.data.cache.TrackCache
import com.ru.androidexperts.muzicapp.data.cache.TrackIdByTermCache
import com.ru.androidexperts.muzicapp.data.cache.TracksDao
import com.ru.androidexperts.muzicapp.data.cache.TracksDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var dao: TracksDao
    private lateinit var database: TracksDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            TracksDatabase::class.java
        ).build()
        dao = database.dao()
        database.clearAllTables()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun test() = runBlocking {
        val termValue = "key"
        val newTerm = TermsCache(id = 1001, term = termValue)
        val tracksList = listOf(
            TrackCache(
                id = 1L,
                trackTitle = "title 1",
                authorName = "author 1",
                coverUrl = "coverUrl 1",
                sourceUrl = "sourceUrl 1"
            ),
            TrackCache(
                id = 2L,
                trackTitle = "title 2",
                authorName = "author 2",
                coverUrl = "coverUrl 2",
                sourceUrl = "sourceUrl 2"
            )
        )

        dao.saveTerm(value = newTerm)
        assertEquals(true, dao.isCached(value = termValue))

        dao.saveTracks(tracksList)
        tracksList.forEach { track ->
            dao.saveTrackIdByTerm(
                TrackIdByTermCache(termId = newTerm.id, trackId = track.id)
            )
        }
        assertEquals(tracksList, dao.tracksByTerm(term = termValue))
    }
}