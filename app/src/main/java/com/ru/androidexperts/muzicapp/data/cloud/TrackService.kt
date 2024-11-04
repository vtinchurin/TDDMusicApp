package com.ru.androidexperts.muzicapp.data.cloud

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackService {

    @GET("search")
    fun search(
        @Query("term") term: String = ""
    ): Call<TrackResponse>
}

data class TrackResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val dataList: List<TrackCloud>
)

data class TrackCloud(
    @SerializedName("trackId")
    val trackId: Long,
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("artworkUrl100")
    val artworkUrl: String,
    @SerializedName("previewUrl")
    val previewUrl: String
)