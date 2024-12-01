package com.ru.androidexperts.muzicapp.search.domain.model

interface TrackModel {

    fun <T : Any> map(mapper: Mapper<T>): T

    data class Base(
        private val id: Long,
        private val trackTitle: String,
        private val authorName: String,
        private val coverUrl: String,
        private val sourceUrl: String,
    ) : TrackModel {
        override fun <T : Any> map(mapper: Mapper<T>) =
            mapper.mapToTrackUi(id, trackTitle, authorName, coverUrl, sourceUrl)
    }

    interface Mapper<T : Any> {

        fun mapToTrackUi(
            id: Long,
            trackTitle: String,
            authorName: String,
            coverUrl: String,
            sourceUrl: String,
        ): T

    }
}
