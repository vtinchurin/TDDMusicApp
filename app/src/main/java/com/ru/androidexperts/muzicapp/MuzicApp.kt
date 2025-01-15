package com.ru.androidexperts.muzicapp

import android.app.Application
import com.ru.androidexperts.muzicapp.di.Core
import com.ru.androidexperts.muzicapp.di.ManageViewModels
import com.ru.androidexperts.muzicapp.di.PicEngine
import com.ru.androidexperts.muzicapp.di.ProvidePicEngine
import com.ru.androidexperts.muzicapp.di.ProvideViewModel
import com.ru.androidexperts.muzicapp.di.ViewModelTag

class MuzicApp : Application(), ProvideViewModel, ProvidePicEngine {

    private lateinit var viewModelsFactory: ManageViewModels
    private lateinit var core: Core

    override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T =
        viewModelsFactory.makeViewModel(clasz)

    override fun onCreate() {
        super.onCreate()
        core = Core(this)
        val make = ProvideViewModel.Make(core)
        viewModelsFactory = ManageViewModels.Factory(make)
    }

    override fun picEngine(): PicEngine =
        core.picEngine
}