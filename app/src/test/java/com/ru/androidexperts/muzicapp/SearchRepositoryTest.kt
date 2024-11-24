package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.data.cache.TrackCache
import com.ru.androidexperts.muzicapp.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.data.cloud.TrackCloud
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.domain.repository.SearchRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchRepositoryTest {

    private lateinit var repository: SearchRepository
    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var cachedTerm: FakeCache
    private lateinit var handleError: HandleError
    private lateinit var mapper: DataException.Mapper<LoadResult.Error>
    private lateinit var order: Order

    @Before
    fun setup() {
        order = Order()
        cacheDataSource = FakeCacheDataSource.Base(order)
        cloudDataSource = FakeCloudDataSource.Base(order)
        cachedTerm = FakeCache.Base(order)
        handleError = HandleError.ToData()
        mapper = DataException.Mapper.ToErrorLoadResult()
        cacheDataSource.setCachedData(CACHED_TRACKS)
        repository = SearchRepository.Base(
            cacheDataSource = cacheDataSource,
            cloudDataSource = cloudDataSource,
            handleError = handleError,
            errorLoadResult = mapper,
            termCache = cachedTerm,
        )
    }

    @Test
    fun `not cached data load`(): Unit = runBlocking {
        val tracks: LoadResult = repository.load(term = "not_cached")
        cacheDataSource.assertContainsCalledCount(count = 1)
        cloudDataSource.assertLoadCalledCount(count = 1)
        cacheDataSource.assertSaveCalledCount(count = 1)
        cacheDataSource.assertResult(CLOUD_AND_CACHED_TRACKS)
        assertEquals(tracks, SUCCESS_CLOUD_LOAD_RESULT)
        cachedTerm.assertValue("not_cached")
        order.check(
            listOf(
                SHARED_PREFS_SAVE,
                CHECK_TERM_CONTAINS,
                CLOUD_LOAD,
                CACHE_SAVE,
                CACHE_LOAD,
            )
        )
    }

    @Test
    fun `cached data load`(): Unit = runBlocking {
        val tracks: LoadResult = repository.load(term = "cached")
        cacheDataSource.assertContainsCalledCount(count = 1)
        cloudDataSource.assertLoadCalledCount(count = 0)
        cacheDataSource.assertSaveCalledCount(count = 0)
        cacheDataSource.assertResult(CACHED_TRACKS)
        assertEquals(tracks, SUCCESS_CACHED_LOAD_RESULT)
        cachedTerm.assertValue("cached")
        order.check(
            listOf(
                SHARED_PREFS_SAVE,
                CHECK_TERM_CONTAINS,
                CACHE_LOAD,
            )
        )
    }

    companion object {
        private val CACHED_TRACKS: List<TrackCache> = listOf(
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
        private val CLOUD_AND_CACHED_TRACKS: List<TrackCache> = listOf(
            TrackCache(
                id = 3L,
                trackTitle = "title 3",
                authorName = "author 3",
                coverUrl = "coverUrl 3",
                sourceUrl = "sourceUrl 3"
            ),
            TrackCache(
                id = 4L,
                trackTitle = "title 4",
                authorName = "author 4",
                coverUrl = "coverUrl 4",
                sourceUrl = "sourceUrl 4"
            )
        )
        private val SUCCESS_CLOUD_LOAD_RESULT: LoadResult = LoadResult.Tracks(
            data = listOf(
                TrackModel.Base(
                    id = 3L,
                    trackTitle = "title 3",
                    authorName = "author 3",
                    coverUrl = "coverUrl 3",
                    sourceUrl = "sourceUrl 3"
                ),
                TrackModel.Base(
                    id = 4L,
                    trackTitle = "title 4",
                    authorName = "author 4",
                    coverUrl = "coverUrl 4",
                    sourceUrl = "sourceUrl 4"
                )
            )
        )
        private val SUCCESS_CACHED_LOAD_RESULT: LoadResult = LoadResult.Tracks(
            data = listOf(
                TrackModel.Base(
                    id = 1L,
                    trackTitle = "title 1",
                    authorName = "author 1",
                    coverUrl = "coverUrl 1",
                    sourceUrl = "sourceUrl 1"
                ),
                TrackModel.Base(
                    id = 2L,
                    trackTitle = "title 2",
                    authorName = "author 2",
                    coverUrl = "coverUrl 2",
                    sourceUrl = "sourceUrl 2"
                )
            )
        )
    }
}

private interface FakeCacheDataSource : CacheDataSource {

    fun assertContainsCalledCount(count: Int)

    fun assertSaveCalledCount(count: Int)

    fun checkLoadCalledCount(count: Int)

    fun assertResult(trackList: List<TrackCache>)

    fun setCachedData(trackList: List<TrackCache>)

    class Base(private val order: Order) : FakeCacheDataSource {

        private val term = "cached"
        private var containsCalledCount = 0
        private var saveCalledCount = 0
        private var loadCalledCount = 0
        private var cachedTrackList = listOf<TrackCache>()

        override fun assertContainsCalledCount(count: Int) {
            assertEquals(count, containsCalledCount)
        }

        override fun assertSaveCalledCount(count: Int) {
            assertEquals(count, saveCalledCount)
        }

        override fun checkLoadCalledCount(count: Int) {
            assertEquals(count, loadCalledCount)
        }

        override fun assertResult(trackList: List<TrackCache>) {
            assertEquals(trackList, cachedTrackList)
        }

        override fun setCachedData(trackList: List<TrackCache>) {
            cachedTrackList = trackList
        }

        override suspend fun isCached(term: String): Boolean {
            containsCalledCount++
            order.add(CHECK_TERM_CONTAINS)
            return this.term == term
        }

        override suspend fun getTracks(term: String): List<TrackCache> {
            order.add(CACHE_LOAD)
            loadCalledCount++
            return cachedTrackList
        }

        override suspend fun saveTracks(term: String, trackList: List<TrackCache>) {
            saveCalledCount++
            order.add(CACHE_SAVE)
            setCachedData(trackList)
        }
    }
}

private interface FakeCloudDataSource : CloudDataSource {

    fun assertLoadCalledCount(count: Int)

    class Base(private val order: Order) : FakeCloudDataSource {

        private var loadCalledCount = 0
        private val tracks: List<TrackCloud> = listOf(
            TrackCloud(
                trackId = 3L,
                trackName = "title 3",
                artistName = "author 3",
                artworkUrl = "coverUrl 3",
                previewUrl = "sourceUrl 3"
            ),
            TrackCloud(
                trackId = 4L,
                trackName = "title 4",
                artistName = "author 4",
                artworkUrl = "coverUrl 4",
                previewUrl = "sourceUrl 4"
            )
        )

        override fun assertLoadCalledCount(count: Int) {
            assertEquals(count, loadCalledCount)
        }

        override suspend fun load(term: String): List<TrackCloud> {
            order.add(CLOUD_LOAD)
            loadCalledCount++
            return if (term == "not_cached")
                tracks
            else listOf()
        }
    }
}

private interface FakeCache : StringCache {

    fun assertValue(value: String)

    class Base(private val order: Order) : FakeCache {

        private var current = ""

        override fun assertValue(value: String) {
            assertEquals(value, current)
        }

        override fun save(value: String) {
            order.add(SHARED_PREFS_SAVE)
            current = value
        }

        override fun restore(): String {
            return current
        }
    }
}

private const val CLOUD_LOAD = "CloudDataSource#load"
private const val CHECK_TERM_CONTAINS = "CacheDataSource#termContains"
private const val CACHE_LOAD = "CacheDataSource#load"
private const val CACHE_SAVE = "CacheDataSource#save"
private const val SHARED_PREFS_SAVE = "SharedPrefsCache#save"
