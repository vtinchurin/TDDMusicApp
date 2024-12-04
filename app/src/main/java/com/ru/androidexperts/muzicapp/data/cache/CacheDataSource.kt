package com.ru.androidexperts.muzicapp.data.cache

interface CacheDataSource {

    suspend fun isCached(term: String): Boolean
    suspend fun saveTracks(term: String, tracks: List<TrackCache>)
    suspend fun getTracks(term: String): List<TrackCache>

    class Base(private val dao: TracksDao) : CacheDataSource {

        override suspend fun isCached(term: String): Boolean {
            return dao.isCached(term)
        }

        override suspend fun saveTracks(term: String, tracks: List<TrackCache>) {
            val termId = dao.saveTerm(TermsCache(term = term))
            val tracksIds = dao.saveTracks(tracks)
            tracksIds.forEach {
                dao.saveTrackIdByTerm(TrackIdByTermCache(termId.toInt(), it))
            }
        }

        override suspend fun getTracks(term: String): List<TrackCache> {
            return dao.tracksByTerm(term)
        }
    }
}