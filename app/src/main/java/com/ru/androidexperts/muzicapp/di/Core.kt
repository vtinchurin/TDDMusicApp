package com.ru.androidexperts.muzicapp.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.SharedPreferencesWrapper
import com.ru.androidexperts.muzicapp.core.cache.StringCache
import com.ru.androidexperts.muzicapp.search.data.cache.CacheModule
import com.ru.androidexperts.muzicapp.search.data.cloud.CloudModule
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist

class Core(context: Context) {

    val runUiTests = try {
        Class.forName(ESPRESSO_CLASS_NAME)
        true
    } catch (e: ClassNotFoundException) {
        false
    }

    private val sharedPreferences = (if (runUiTests) SharedPreferencesWrapper.Test
    else SharedPreferencesWrapper.Base(
        context.getString(R.string.app_name)
    )).sharedPreferences(context)

    val picEngine = if (runUiTests) PicEngine.Test else PicEngine.Base

    val termCache = StringCache.Base(TERM_CACHE_KEY, sharedPreferences, "")

    val cacheModule: CacheModule = CacheModule.Base(context)

    val cloudModule = CloudModule.Base(API_URL)

    val exoPlayer = ExoPlayer.Builder(context).build()

    val observable: Playlist<SearchUiState> = Playlist.Base()

    companion object {
        private const val API_URL = "https://itunes.apple.com/"
        private const val ESPRESSO_CLASS_NAME = "com.ru.androidexperts.muzicapp.ScenarioTest"
        private const val TERM_CACHE_KEY = "termKey"
    }
}