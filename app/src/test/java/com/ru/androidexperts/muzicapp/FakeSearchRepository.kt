package com.ru.androidexperts.muzicapp

import org.junit.Assert.assertEquals

interface FakeSearchRepository : SearchRepository {
    fun expectTrackList(list: List<Track>)
    fun assertLoadCalledCount(expectedCount: Int)
    fun expectTermCached(termCache: String)
    fun expectError()
    fun expectSuccess()

    class Base(
        private val order: Order,
    ) : FakeSearchRepository {

        private var expectedTrackList: List<Track> = listOf()
        private var loadCalledCount = 0
        private var termCached = ""
        private var expectError = false

        override fun expectTrackList(list: List<Track>) {
            expectedTrackList = list
        }

        override fun assertLoadCalledCount(expectedCount: Int) {
            assertEquals(expectedCount, loadCalledCount)
        }

        override fun expectTermCached(termCache: String) {
            this.termCached = termCache
        }

        override suspend fun load(term: String): LoadResult {
            loadCalledCount++
            order.add(REPOSITORY_LOAD)
            if (expectError)
                throw IllegalStateException("No internet connection")
            return LoadResult.Seccess(expectedTrackList)
        }

        override fun termCached(): String {
            order.add(REPOSITORY_TERM)
            return termCached
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