package com.ru.androidexperts.muzicapp.search

import com.ru.androidexperts.muzicapp.search.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.search.data.cloud.TrackCloud
import com.ru.androidexperts.muzicapp.search.data.cloud.TrackService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitTest {

    private lateinit var cloudDataSource: CloudDataSource

    @Before
    fun setup() {
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
        cloudDataSource = CloudDataSource.Base(
            retrofit.create(TrackService::class.java),
        )
    }

    @Test
    fun test_load(): Unit = runBlocking {
        try {
            val dataList = cloudDataSource.load("Dropkick Murphys")
            assertEquals(12, dataList.size)

            val expected = TrackCloud(
                trackId=264539104,
                trackName="Tomorrow's Industry",
                artistName="Dropkick Murphys",
                artworkUrl="https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/69/30/e2/6930e28c-02b8-46a4-2dfe-cfb8511fe315/075597996609.jpg/100x100bb.jpg",
                previewUrl="https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/4f/dc/95/4fdc95b2-ba1d-4306-2faf-783a3698f4f7/mzaf_7008876201452666705.plus.aac.p.m4a"
            )
            assertEquals(expected, dataList[0])
        } catch (e: Exception) {
            val exception = when (e) {
                is IOException -> NoInternetConnectionException
                else -> ServiceUnavailable()
            }
            assertEquals(exception, NoInternetConnectionException)
        }
    }
}

object NoInternetConnectionException : Exception()

class ServiceUnavailable : Exception()