package com.ru.androidexperts.muzicapp.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.data.cache.TracksDatabase
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist

class Core(context: Context) {

    val runUiTests = true

    private val sharedPrefsName = if (runUiTests) "test" else context.getString(R.string.app_name)
    val sharedPreferences = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)

    val exoPlayer = ExoPlayer.Builder(context).build()

    val database = Room.databaseBuilder(
        context,
        TracksDatabase::class.java,
        context.getString(R.string.app_name)
    ).build()

    val observable: Playlist<SearchUiState> = Playlist.Base()
}