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
            assertEquals(10, dataList.size)

            val expected = TrackCloud(
                trackId=1485050464,
                trackName="I'm Shipping up to Boston",
                artistName="Dropkick Murphys",
                artworkUrl="https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/51/87/bd/5187bdfd-c95c-910f-cbd7-920886443c0d/0045778047260.png/100x100bb.jpg",
                previewUrl="https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/54/10/ee/5410ee29-31c3-33d9-f793-1d0fa46bfa20/mzaf_14032484010004764652.plus.aac.p.m4a"
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