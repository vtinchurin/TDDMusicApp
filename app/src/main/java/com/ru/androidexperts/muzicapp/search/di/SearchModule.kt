package com.ru.androidexperts.muzicapp.search.di

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.core.player.MusicPlayerBase
import com.ru.androidexperts.muzicapp.di.Core
import com.ru.androidexperts.muzicapp.di.Module
import com.ru.androidexperts.muzicapp.search.data.DataException
import com.ru.androidexperts.muzicapp.search.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.search.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.search.data.cloud.TrackService
import com.ru.androidexperts.muzicapp.search.data.repository.SearchRepositoryBase
import com.ru.androidexperts.muzicapp.search.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.search.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.search.presentation.mappers.UiMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchModule(private val core: Core) : Module<SearchViewModel> {

    override fun viewModel(): SearchViewModel {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .readTimeout(HTTP_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val repository = SearchRepositoryBase(
            cacheDataSource = CacheDataSource.Base(
                dao = core.cacheModule.dao()
            ),
            cloudDataSource = CloudDataSource.Base(
                service = retrofit.create(TrackService::class.java)
            ),
            handleError = HandleError.ToData(),
            errorLoadResult = DataException.Mapper.ToErrorLoadResult(),
            termCache = core.termCache
        )

        val runAsync: RunAsync = RunAsync.Search()

        val player: MusicPlayer = MusicPlayerBase(core.exoPlayer)

        return SearchViewModel(
            observable = core.observable,
            runAsync = runAsync,
            repository = repository,
            player = player,
            toUi = UiMapper.Base(),
            toPlayList = PlayerMapper.Base()
        )
    }

    companion object {

        private const val API_URL = "https://itunes.apple.com/"
        private const val HTTP_CLIENT_READ_TIMEOUT = 60L
        private const val HTTP_CLIENT_WRITE_TIMEOUT = 60L
        private const val HTTP_CLIENT_CONNECT_TIMEOUT = 60L
    }
}