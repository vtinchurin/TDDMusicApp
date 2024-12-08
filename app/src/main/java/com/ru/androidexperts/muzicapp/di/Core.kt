package com.ru.androidexperts.muzicapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.media3.exoplayer.ExoPlayer
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.search.data.cache.CacheModule
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist

class Core(context: Context) {

    val runUiTests = try {
        Class.forName(ESPRESSO_CLASS_NAME)
        true
    } catch (e: ClassNotFoundException) {
        false
    }

    private val sharedPrefsName =
        if (runUiTests) TEST_SHARED_PREFS_NAME else context.getString(R.string.app_name)
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        sharedPrefsName,
        Context.MODE_PRIVATE
    )
    val termCache = StringCache.Base(TERM_CACHE_KEY, sharedPreferences, "")

    val cacheModule: CacheModule = CacheModule.Base(context)

    val exoPlayer = ExoPlayer.Builder(context).build()

    val observable: Playlist<SearchUiState> = Playlist.Base()

    companion object {

        private const val TEST_SHARED_PREFS_NAME = "test"
        private const val ESPRESSO_CLASS_NAME = "androidx.test.ext.junit.runners.AndroidJUnit4"
        private const val TERM_CACHE_KEY = "termKey"
    }
}