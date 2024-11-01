package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ru.androidexperts.muzicapp.data.cache.SearchKeysCache
import com.ru.androidexperts.muzicapp.data.cache.TrackCache
import com.ru.androidexperts.muzicapp.data.cache.TrackIdByKeysCache
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
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun test() = runBlocking {
        val searchKey = SearchKeysCache("key")
        val tracksList = listOf(
            TrackCache(
                id = 1L,
                trackTitle = "title",
                authorName = "author",
                coverUrl = "coverUrl",
                sourceUrl = "sourceUrl"
            ),
            TrackCache(
                id = 2L,
                trackTitle = "title",
                authorName = "author",
                coverUrl = "coverUrl",
                sourceUrl = "sourceUrl"
            )
        )

        dao.saveKey(searchKey = searchKey)
        assertEquals(searchKey, dao.key("key"))

        dao.saveTracks(tracksList)
        assertEquals(tracksList[0], dao.track(1L))
        assertEquals(tracksList[1], dao.track(2L))

        tracksList.forEach { track ->
            dao.saveTrackIdByKey(TrackIdByKeysCache(searchKey = searchKey.key, trackId = track.id))
        }

        val trackIds = dao.trackIdsByKey(searchKey.key)
        assertEquals(listOf(1L, 2L), trackIds)
    }
}