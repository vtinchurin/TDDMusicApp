package com.ru.androidexperts.muzicapp.search.data.cloud

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface CloudModule {

    fun <T : Any> provideService(clazz: Class<T>): T
    fun <T : Any> provideService(baseUrl: String, clazz: Class<T>): T

    class Base : CloudModule {

        private val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .readTimeout(HTTP_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())

        override fun <T : Any> provideService(clazz: Class<T>): T =
            builder.build().create(clazz)

        override fun <T : Any> provideService(baseUrl: String, clazz: Class<T>): T =
            builder.baseUrl(baseUrl).build().create(clazz)
    }

    companion object {
        private const val HTTP_CLIENT_READ_TIMEOUT = 60L
        private const val HTTP_CLIENT_WRITE_TIMEOUT = 60L
        private const val HTTP_CLIENT_CONNECT_TIMEOUT = 60L
        private const val API_URL = "https://itunes.apple.com/"
    }
}