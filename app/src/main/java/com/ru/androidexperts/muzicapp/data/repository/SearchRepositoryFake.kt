package com.ru.androidexperts.muzicapp.data.repository

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.domain.model.LoadResult
import com.ru.androidexperts.muzicapp.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.domain.repository.SearchRepository
import kotlinx.coroutines.delay
import java.io.IOException

class SearchRepositoryFake(
    private val handleError: HandleError,
    private val errorLoadResult: DataException.Mapper<LoadResult.Error>,
    private val tracks: List<TrackModel> = listOf(
        TrackModel.Base(
            id = 0L,
            trackTitle = "Song 1",
            authorName = "ExistentArtist",
            coverUrl = "cover url 1",
            sourceUrl = "source url 1"
        ),
        TrackModel.Base(
            id = 1L,
            trackTitle = "Song 2",
            authorName = "ExistentArtist",
            coverUrl = "cover url 2",
            sourceUrl = "source url 2"
        )
    ),
    private val termCache: StringCache
) : SearchRepository {

    private var loadCalledCount = 0

    override fun lastCachedTerm(): String {
        return termCache.restore()
    }

    override suspend fun load(term: String): LoadResult {
        delay(1500)
        loadCalledCount++
        termCache.save(value = term)
        try {
            if(term.isEmpty())
                return LoadResult.Empty

            if (loadCalledCount == 1)
                throw IOException()

            if (term == NON_EXISTENT_ARTIST)
                return LoadResult.NoTracks

            if (term == EXISTENT_ARTIST)
                return LoadResult.Tracks(tracks)
        } catch (e: Exception) {
            val dataException = handleError.handleError(e)
            return dataException.map(errorLoadResult)
        }

        throw IllegalStateException(
            """Wrong term value, allowed values are: $EXISTENT_ARTIST, $NON_EXISTENT_ARTIST
                        |current value: $term
                    """.trimMargin()
        )
    }

    companion object {
        private val EXISTENT_ARTIST = "ExistentArtist"
        private val NON_EXISTENT_ARTIST = "NonExistentArtist"
    }
}