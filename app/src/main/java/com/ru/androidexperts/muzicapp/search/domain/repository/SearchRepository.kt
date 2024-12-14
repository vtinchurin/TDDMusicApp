package com.ru.androidexperts.muzicapp.search.domain.repository

import com.ru.androidexperts.muzicapp.search.domain.model.LoadResult

interface SearchRepository {

    fun lastCachedTerm(): String

    suspend fun load(term: String): LoadResult
}