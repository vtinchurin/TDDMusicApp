package com.ru.androidexperts.muzicapp.di.core.viewmodels

import com.ru.androidexperts.muzicapp.di.core.Core
import com.ru.androidexperts.muzicapp.di.core.Module

abstract class AbstractProvideViewModel(
    protected val core: Core,
    private val nextChain: ProvideViewModel,
    private val viewModelClass: Class<out ViewModelTag>
) : ProvideViewModel {

    override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T {
        return if (clasz == viewModelClass)
            module().viewModel() as T
        else
            nextChain.makeViewModel(clasz)
    }

    protected abstract fun module(): Module<*>
}