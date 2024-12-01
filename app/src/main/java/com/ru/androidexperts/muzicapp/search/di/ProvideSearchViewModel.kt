package com.ru.androidexperts.muzicapp.search.di

import com.ru.androidexperts.muzicapp.di.AbstractProvideViewModel
import com.ru.androidexperts.muzicapp.di.Core
import com.ru.androidexperts.muzicapp.di.ProvideViewModel
import com.ru.androidexperts.muzicapp.search.presentation.SearchViewModel

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