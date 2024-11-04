package com.ru.androidexperts.muzicapp.data.cloud

interface CloudDataSource {

    suspend fun load(term: String): List<TrackCloud>

    class Base(
        private val service: TrackService
    ) : CloudDataSource {

        override suspend fun load(term: String): List<TrackCloud> {
            val result = service.search(term).execute()
            if (result.isSuccessful) {
                val body = result.body()!!
                return body.dataList
            } else {
                throw IllegalStateException((result.errorBody().toString()))
            }
        }
    }
}