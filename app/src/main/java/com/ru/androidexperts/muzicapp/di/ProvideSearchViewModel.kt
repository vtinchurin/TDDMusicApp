package com.ru.androidexperts.muzicapp.di

import com.ru.androidexperts.muzicapp.di.core.Core
import com.ru.androidexperts.muzicapp.di.core.viewmodels.AbstractProvideViewModel
import com.ru.androidexperts.muzicapp.di.core.viewmodels.ProvideViewModel
import com.ru.androidexperts.muzicapp.presentation.SearchViewModel

class ProvideSearchViewModel(
    core: Core,
    next: ProvideViewModel
) : AbstractProvideViewModel(
    core = core,
    nextChain = next,
    viewModelClass = SearchViewModel::class.java
) {

    override fun module() = SearchModule(core)
}