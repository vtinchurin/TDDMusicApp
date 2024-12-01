package com.ru.androidexperts.muzicapp

import android.app.Application
import com.ru.androidexperts.muzicapp.di.core.Core
import com.ru.androidexperts.muzicapp.di.core.viewmodels.ManageViewModels
import com.ru.androidexperts.muzicapp.di.core.viewmodels.ProvideViewModel
import com.ru.androidexperts.muzicapp.di.core.viewmodels.ViewModelTag

class MuzicApp : Application(), ProvideViewModel {

    private lateinit var viewModelsFactory: ManageViewModels

    override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T =
        viewModelsFactory.makeViewModel(clasz)

    override fun onCreate() {
        super.onCreate()

        val make = ProvideViewModel.Make(Core(this))
        viewModelsFactory = ManageViewModels.Factory(make)
    }
}