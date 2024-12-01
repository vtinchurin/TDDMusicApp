package com.ru.androidexperts.muzicapp.di.core

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.SearchUiState
import com.ru.androidexperts.muzicapp.data.cache.TracksDatabase
import com.ru.androidexperts.muzicapp.uiObservable.UiObservable

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

    val observable: UiObservable.Playlist<SearchUiState> = UiObservable.Playlist.Base()
}