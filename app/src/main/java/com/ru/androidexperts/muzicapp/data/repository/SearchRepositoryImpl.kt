package com.ru.androidexperts.muzicapp.data.repository

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.data.cache.TrackCache
import com.ru.androidexperts.muzicapp.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.ResultEntityModel
import com.ru.androidexperts.muzicapp.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val cacheDataSource: CacheDataSource,
    private val cloudDataSource: CloudDataSource,
    private val handleError: HandleError,
    private val mapper: DataException.Mapper<ResultEntityModel>,
    private val termCache: StringCache,
) : SearchRepository {

    override fun lastCachedTerm(): String = termCache.restore()

    override suspend fun load(term: String): LoadResult {
        try {
            if (!cacheDataSource.isCached(term)) {
                val result = cloudDataSource.load(term)
                if (result.isEmpty())
                    return LoadResult.Empty
                val tracksToCache = result.map { trackCloud ->
                    TrackCache(
                        id = trackCloud.trackId,
                        trackTitle = trackCloud.trackName,
                        authorName = trackCloud.artistName,
                        coverUrl = trackCloud.artworkUrl,
                        sourceUrl = trackCloud.previewUrl
                    )
                }
                cacheDataSource.saveTracks(term = term, tracks = tracksToCache)
            }
            val tracks = cacheDataSource.getTracks(term).map { trackCache ->
                ResultEntityModel.Track(
                    id = trackCache.id,
                    trackTitle = trackCache.trackTitle,
                    authorName = trackCache.authorName,
                    coverUrl = trackCache.coverUrl,
                    sourceUrl = trackCache.sourceUrl,
                )
            }
            termCache.save(value = term)
            return LoadResult.Tracks(tracks)
        } catch (e: Exception) {
            val dataException = handleError.handleError(e)
            val error = dataException.map(mapper)
            return LoadResult.Error(error)
        }
    }
}