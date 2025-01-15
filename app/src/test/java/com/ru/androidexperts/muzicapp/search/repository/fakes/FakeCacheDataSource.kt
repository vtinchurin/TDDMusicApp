package com.ru.androidexperts.muzicapp.search.repository.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.search.data.cache.TrackCache

interface FakeCacheDataSource : CacheDataSource {

    fun assertIsCachedCalled(expectedTerm: String)
    fun assertGetTracksCalled(expectedTerm: String)
    fun assertSaveTracksCalled(expectedTerm: String, expectedTracks: List<TrackCache>)
    fun setCachedData(trackList: List<TrackCache>)

    class Base(private val order: Order) : FakeCacheDataSource {

        private val term = "cached"
        private var cachedTrackList = listOf<TrackCache>()

        override fun setCachedData(trackList: List<TrackCache>) {
            cachedTrackList = trackList
        }

        override fun assertIsCachedCalled(expectedTerm: String) {
            order.assert(CHECK_TERM_CONTAINS, expectedTerm)
        }

        override fun assertGetTracksCalled(expectedTerm: String) {
            order.assert(CACHE_LOAD, expectedTerm)
        }

        override fun assertSaveTracksCalled(
            expectedTerm: String,
            expectedTracks: List<TrackCache>,
        ) {
            order.assert(CACHE_SAVE, listOf(expectedTerm, expectedTracks))
        }

        override suspend fun isCached(term: String): Boolean {
            order.add(CHECK_TERM_CONTAINS, term)
            return this.term == term
        }

        override suspend fun getTracks(term: String): List<TrackCache> {
            order.add(CACHE_LOAD, term)
            return cachedTrackList
        }

        override suspend fun saveTracks(term: String, tracks: List<TrackCache>) {
            order.add(CACHE_SAVE, listOf(term, tracks))
            cachedTrackList = tracks
        }
    }

    companion object {
        private const val CHECK_TERM_CONTAINS = "CacheDataSource#termContains"
        private const val CACHE_LOAD = "CacheDataSource#load"
        private const val CACHE_SAVE = "CacheDataSource#save"
    }
}