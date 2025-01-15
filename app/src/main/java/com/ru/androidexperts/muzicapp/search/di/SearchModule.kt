package com.ru.androidexperts.muzicapp.search.di

import com.ru.androidexperts.muzicapp.core.HandleError
import com.ru.androidexperts.muzicapp.core.RunAsync
import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.core.player.MusicPlayerBase
import com.ru.androidexperts.muzicapp.di.Core
import com.ru.androidexperts.muzicapp.di.Module
import com.ru.androidexperts.muzicapp.search.data.DataException
import com.ru.androidexperts.muzicapp.search.data.cache.CacheDataSource
import com.ru.androidexperts.muzicapp.search.data.cloud.CloudDataSource
import com.ru.androidexperts.muzicapp.search.data.cloud.TrackService
import com.ru.androidexperts.muzicapp.search.data.repository.SearchRepositoryBase
import com.ru.androidexperts.muzicapp.search.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.search.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.search.presentation.mappers.UiMapper

class SearchModule(private val core: Core) : Module<SearchViewModel> {

    override fun viewModel(): SearchViewModel {

        val repository = SearchRepositoryBase(
            cacheDataSource = CacheDataSource.Base(
                dao = core.cacheModule.dao()
            ),
            cloudDataSource = CloudDataSource.Base(
                service = core.cloudModule.provideService(TrackService::class.java)
            ),
            handleError = HandleError.ToData(),
            errorLoadResult = DataException.Mapper.ToErrorLoadResult(),
            termCache = core.termCache
        )

        val runAsync: RunAsync = RunAsync.Search()

        val player: MusicPlayer = MusicPlayerBase(core.exoPlayer)

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