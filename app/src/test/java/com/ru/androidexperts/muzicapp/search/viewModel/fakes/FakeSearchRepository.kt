package com.ru.androidexperts.muzicapp.search.viewModel.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.search.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.search.domain.repository.SearchRepository

interface FakeSearchRepository : SearchRepository {

    fun expectTrackList(list: List<TrackModel>)

    fun expectTermCached(termCache: String)

    fun expectError()

    fun expectSuccess()

    fun assertLoadCalled(term: String)

    fun assertLastTermCachedCalled()

    data class Base(private val order: Order) : FakeSearchRepository {

        private var expectError = false
        private var expectedTrackList: List<TrackModel> = listOf()
        private var termCached = ""

        override fun lastCachedTerm(): String {
            order.add(REPOSITORY_TERM)
            return termCached
        }

        override fun expectTrackList(list: List<TrackModel>) {
            expectedTrackList = list
        }

        override fun expectTermCached(termCache: String) {
            termCached = termCache
        }

        override fun assertLoadCalled(term: String) {
            order.assert(REPOSITORY_LOAD, term)
        }

        override fun assertLastTermCachedCalled() {
            order.assert(REPOSITORY_TERM)
        }

        override fun expectError() {
            expectError = true
        }

        override fun expectSuccess() {
            expectError = false
        }

        override suspend fun load(term: String): LoadResult {
            //TODO REFACTOR WITH TROWING EXCEPTION
            order.add(REPOSITORY_LOAD, term)
            termCached = term
            return if (expectError)
                LoadResult.Error(errorResId = -777)
            else if (expectedTrackList.isEmpty())
                LoadResult.NoTracks
            else LoadResult.Tracks(expectedTrackList)
        }
    }

    companion object {
        private const val REPOSITORY_LOAD = "Repository#load"
        private const val REPOSITORY_TERM = "Repository#term"
    }
}