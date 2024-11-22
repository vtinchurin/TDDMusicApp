package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.ResultEntityModel
import com.ru.androidexperts.muzicapp.domain.repository.SearchRepository
import okio.IOException
import org.junit.Assert.assertEquals

interface FakeSearchRepository : SearchRepository {

    fun expectTrackList(list: List<ResultEntityModel>)

    fun assertLoadCalledCount(expectedCount: Int)

    fun expectTermCached(termCache: String)

    fun expectError()

    fun expectSuccess()

    class Base(
        private val order: Order,
    ) : FakeSearchRepository {

        private var expectedTrackList: List<ResultEntityModel> = listOf()
        private var loadCalledCount = 0
        private var termCached = ""
        private var expectError = false
        private val handleError = HandleError.ToData()
        private val mapper = DataException.Mapper.ToDomain()

        override suspend fun load(term: String): LoadResult {
            try {
                loadCalledCount++
                order.add(REPOSITORY_LOAD)
                if (expectError)
                    throw IOException("No internet connection")
                return LoadResult.Tracks(expectedTrackList)
            } catch (e: Exception) {
                val dataException = handleError.handleError(e)
                val error = dataException.map(mapper)
                return LoadResult.Error(error)
            }
        }

        override fun lastCachedTerm(): String {
            order.add(REPOSITORY_TERM)
            return termCached
        }

        override fun expectTrackList(list: List<ResultEntityModel>) {
            expectedTrackList = list
        }

        override fun assertLoadCalledCount(expectedCount: Int) {
            assertEquals(expectedCount, loadCalledCount)
        }

        override fun expectTermCached(termCache: String) {
            this.termCached = termCache
        }

        override fun expectError() {
            expectError = true
        }

        override fun expectSuccess() {
            expectError = false
        }
    }
}

const val REPOSITORY_LOAD = "repository load"
const val REPOSITORY_TERM = "repository term"