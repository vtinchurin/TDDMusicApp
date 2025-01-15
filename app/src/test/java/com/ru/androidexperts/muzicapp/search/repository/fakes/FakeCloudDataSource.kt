package com.ru.androidexperts.muzicapp.search.repository.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.search.data.cloud.TrackCloud

interface FakeCloudDataSource : CloudDataSource {

    fun expectError()
    fun assertLoadCalled(expectedTerm: String)

    class Base(private val order: Order) : FakeCloudDataSource {

        private var expectedError = false


        private val tracks: List<TrackCloud> = listOf(
            TrackCloud(
                trackId = 3L,
                trackName = "title 3",
                artistName = "author 3",
                artworkUrl = "coverUrl 3",
                previewUrl = "sourceUrl 3"
            ), TrackCloud(
                trackId = 4L,
                trackName = "title 4",
                artistName = "author 4",
                artworkUrl = "coverUrl 4",
                previewUrl = "sourceUrl 4"
            )
        )

        override fun expectError() {
            expectedError = true
        }

        override fun assertLoadCalled(expectedTerm: String) {
            order.assert(CLOUD_LOAD, expectedTerm)
        }


        override suspend fun load(term: String): List<TrackCloud> {
            if (expectedError) throw Exception()
            order.add(CLOUD_LOAD, term)
            return if (term == "not_cached") tracks
            else listOf()
        }
    }

    companion object {
        private const val CLOUD_LOAD = "CloudDataSource#load"
    }
}