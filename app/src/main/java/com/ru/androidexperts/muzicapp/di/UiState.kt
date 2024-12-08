package com.ru.androidexperts.muzicapp.di

import com.ru.androidexperts.muzicapp.core.Navigation

interface UiState {

    fun navigate(navigate: Navigation) = Unit
}