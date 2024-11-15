package com.ru.androidexperts.muzicapp.domain.model

interface ResultEntityModel {

    fun <T : Any> map(mapper: Mapper<T>): T

    data class Track(
        private val id: Long,
        private val trackTitle: String,
        private val authorName: String,
        private val coverUrl: String,
        private val sourceUrl: String,
    ) : ResultEntityModel {
        override fun <T : Any> map(mapper: Mapper<T>) =
            mapper.mapToTrackUi(id, trackTitle, authorName, coverUrl, sourceUrl)
    }

    data class Error(
        private val resId: Int,
    ) : ResultEntityModel {

        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapToError(resId)
        }
    }

    interface Mapper<T : Any> {

        fun mapToTrackUi(
            id: Long,
            trackTitle: String,
            authorName: String,
            coverUrl: String,
            sourceUrl: String,
        ): T

        fun mapToNoTracks(): T

        fun mapToError(resId: Int): T
    }
}
