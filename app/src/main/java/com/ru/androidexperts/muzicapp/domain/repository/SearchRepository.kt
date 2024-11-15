package com.ru.androidexperts.muzicapp.domain.repository

import com.ru.androidexperts.muzicapp.domain.model.LoadResult

interface SearchRepository {

    fun lastCachedTerm(): String

    suspend fun load(term: String): LoadResult

}