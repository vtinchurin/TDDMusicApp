package com.ru.androidexperts.muzicapp

import android.app.Application
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.data.DataException
import com.ru.androidexperts.muzicapp.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.data.cache.TracksDatabase
import com.ru.androidexperts.muzicapp.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.data.cloud.TrackService
import com.ru.androidexperts.muzicapp.data.repository.SearchRepositoryImpl
import com.ru.androidexperts.muzicapp.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.presentation.mappers.UiMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MuzicApp : Application() {

    lateinit var searchViewModel: SearchViewModel

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(
            applicationContext,
            TracksDatabase::class.java,
            applicationContext.getString(R.string.app_name)
        ).build()

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

        val sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.app_name), Context.MODE_PRIVATE
        )

        searchViewModel = SearchViewModel(
            repository = SearchRepositoryImpl(
                cacheDataSource = CacheDataSource.Base(
                    dao = database.dao()
                ),
                cloudDataSource = CloudDataSource.Base(
                    service = retrofit.create(TrackService::class.java)
                ),
                handleError = HandleError.ToData(),
                errorLoadResult = DataException.Mapper.ToErrorLoadResult(),
                termCache = StringCache.Base(
                    "termKey", sharedPreferences, ""
                )
            ),
            player = MusicPlayer.Base(player = ExoPlayer.Builder(this).build()),
            toUi = UiMapper.Base(),
            toPlayList = PlayerMapper.Base()
        )
    }
}