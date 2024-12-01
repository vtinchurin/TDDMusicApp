package com.ru.androidexperts.muzicapp.di.core

import com.ru.androidexperts.muzicapp.di.core.viewmodels.ViewModelTag

interface Module<T : ViewModelTag> {

    fun viewModel(): T
}