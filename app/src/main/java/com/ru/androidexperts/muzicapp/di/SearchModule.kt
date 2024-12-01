package com.ru.androidexperts.muzicapp.di

import com.ru.androidexperts.muzicapp.MusicPlayer
import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.data.cloud.TrackService
import com.ru.androidexperts.muzicapp.data.repository.SearchRepositoryBase
import com.ru.androidexperts.muzicapp.data.repository.SearchRepositoryFake
import com.ru.androidexperts.muzicapp.di.core.Core
import com.ru.androidexperts.muzicapp.di.core.Module
import com.ru.androidexperts.muzicapp.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.presentation.mappers.UiMapper
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
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val repository = if (core.runUiTests)
            SearchRepositoryFake(
                handleError = HandleError.ToData(),
                errorLoadResult = DataException.Mapper.ToErrorLoadResult(),
                termCache = StringCache.Base(
                    "termKey", core.sharedPreferences, ""
                )
            )
        else
            SearchRepositoryBase(
                cacheDataSource = CacheDataSource.Base(
                    dao = core.database.dao()
                ),
                cloudDataSource = CloudDataSource.Base(
                    service = retrofit.create(TrackService::class.java)
                ),
                handleError = HandleError.ToData(),
                errorLoadResult = DataException.Mapper.ToErrorLoadResult(),
                termCache = StringCache.Base(
                    "termKey", core.sharedPreferences, ""
                )
            )

        val runAsync: RunAsync = RunAsync.Search()

        val player: MusicPlayer = if (core.runUiTests)
            MusicPlayer.TestPlayer()
        else
            MusicPlayer.Base(core.exoPlayer)

        return SearchViewModel(
            observable = core.observable,
            runAsync = runAsync,
            repository = repository,
            player = player,
            toUi = UiMapper.Base(),
            toPlayList = PlayerMapper.Base()
        )
    }
}