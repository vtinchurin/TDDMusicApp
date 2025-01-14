package com.ru.androidexperts.muzicapp.search.repository

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.data.DataException
import com.ru.androidexperts.muzicapp.search.data.cache.TrackCache
import com.ru.androidexperts.muzicapp.search.data.repository.SearchRepositoryBase
import com.ru.androidexperts.muzicapp.search.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.search.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.search.domain.repository.SearchRepository
import com.ru.androidexperts.muzicapp.search.repository.fakes.FakeCache
import com.ru.androidexperts.muzicapp.search.repository.fakes.FakeCacheDataSource
import com.ru.androidexperts.muzicapp.search.repository.fakes.FakeCloudDataSource
import com.ru.androidexperts.muzicapp.search.repository.fakes.FakeHandleError
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchRepositoryTest {

    private lateinit var repository: SearchRepository
    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var cachedTerm: FakeCache
    private lateinit var handleError: FakeHandleError
    private lateinit var mapper: DataException.Mapper<LoadResult.Error>
    private lateinit var order: Order

    @Before
    fun setup() {
        order = Order.Base()
        cacheDataSource = FakeCacheDataSource.Base(order)
        cloudDataSource = FakeCloudDataSource.Base(order)
        cachedTerm = FakeCache.Base(order)
        handleError = FakeHandleError.Base(order)
        mapper = DataException.Mapper.ToErrorLoadResult()
        cacheDataSource.setCachedData(CACHED_TRACKS)
        repository = SearchRepositoryBase(
            cacheDataSource = cacheDataSource,
            cloudDataSource = cloudDataSource,
            handleError = handleError,
            errorLoadResult = mapper,
            termCache = cachedTerm,
        )
    }

    @Test
    fun `not cached data load`(): Unit = runBlocking {
        /* Action */
        val tracks: LoadResult = repository.load(term = "not_cached")

        /* Assertion */
        cachedTerm.assertSave("not_cached")
        cacheDataSource.assertIsCachedCalled(expectedTerm = "not_cached")
        cloudDataSource.assertLoadCalled(expectedTerm = "not_cached")
        cacheDataSource.assertSaveTracksCalled(
            expectedTerm = "not_cached",
            expectedTracks = CLOUD_AND_CACHED_TRACKS
        )
        cacheDataSource.assertGetTracksCalled(expectedTerm = "not_cached")
        assertEquals(tracks, SUCCESS_CLOUD_LOAD_RESULT)

        order.assertTraceSize(5)
    }

    @Test
    fun `cached data load`(): Unit = runBlocking {
        /* Action */
        val tracks: LoadResult = repository.load(term = "cached")

        /* Assertion */
        cachedTerm.assertSave("cached")
        cacheDataSource.assertIsCachedCalled(expectedTerm = "cached")
        cacheDataSource.assertGetTracksCalled(expectedTerm = "cached")
        assertEquals(tracks, SUCCESS_CACHED_LOAD_RESULT)

        order.assertTraceSize(3)
    }

    @Test
    fun `last term correctly saved`(): Unit = runBlocking {
        /* Action */
        repository.load("data")

        /* Assertion */
        cachedTerm.assertSave("data")
        cacheDataSource.assertIsCachedCalled("data")
        cloudDataSource.assertLoadCalled("data")
        val result = repository.lastCachedTerm()
        cachedTerm.assertRestore()
        assertEquals(result, "data")

        order.assertTraceSize(4)
    }

    @Test
    fun `fetch empty term`(): Unit = runBlocking {
        /* Action */
        val tracks: LoadResult = repository.load("")

        /* Assertion */
        cachedTerm.assertSave("")
        assertEquals(tracks, LoadResult.Empty)

        order.assertTraceSize(1)
    }

    @Test
    fun `error result`(): Unit = runBlocking {
        cloudDataSource.expectError()
        /* Action */
        val result: LoadResult = repository.load("query")

        /* Assertion */
        cachedTerm.assertSave("query")
        cacheDataSource.assertIsCachedCalled("query")
        assertEquals(result, LoadResult.Error(-777))

        order.assertTraceSize(2)
    }

    @Test
    fun `no tracks result`(): Unit = runBlocking {
        /* Action */
        val result: LoadResult = repository.load("no data")

        /* Assertion */
        cachedTerm.assertSave("no data")
        cacheDataSource.assertIsCachedCalled("no data")
        cloudDataSource.assertLoadCalled("no data")
        assertEquals(result, LoadResult.NoTracks)

        order.assertTraceSize(3)
    }

    companion object {
        private val CACHED_TRACKS: List<TrackCache> = listOf(
            TrackCache(
                id = 1L,
                trackTitle = "title 1",
                authorName = "author 1",
                coverUrl = "coverUrl 1",
                sourceUrl = "sourceUrl 1"
            ), TrackCache(
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
            ), TrackCache(
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
                ), TrackModel.Base(
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
                ), TrackModel.Base(
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

