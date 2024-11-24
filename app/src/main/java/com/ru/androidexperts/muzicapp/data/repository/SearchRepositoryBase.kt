package com.ru.androidexperts.muzicapp.data.repository

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.data.cache.TrackCache
import com.ru.androidexperts.muzicapp.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.domain.repository.SearchRepository

class SearchRepositoryBase(
    private val cacheDataSource: CacheDataSource,
    private val cloudDataSource: CloudDataSource,
    private val handleError: HandleError,
    private val errorLoadResult: DataException.Mapper<LoadResult.Error>,
    private val termCache: StringCache,
) : SearchRepository {

    override fun lastCachedTerm(): String = termCache.restore()

    override suspend fun load(term: String): LoadResult {
        termCache.save(value = term)
        if (term.isEmpty())
            return LoadResult.Empty
        try {
            if (!cacheDataSource.isCached(term)) {
                val result = cloudDataSource.load(term)
                if (result.isEmpty())
                    return LoadResult.NoTracks
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
                TrackModel.Base(
                    id = trackCache.id,
                    trackTitle = trackCache.trackTitle,
                    authorName = trackCache.authorName,
                    coverUrl = trackCache.coverUrl,
                    sourceUrl = trackCache.sourceUrl,
                )
            }
            return LoadResult.Tracks(tracks)
        } catch (e: Exception) {
            val dataException = handleError.handleError(e)
            return dataException.map(errorLoadResult)
        }
    }
}