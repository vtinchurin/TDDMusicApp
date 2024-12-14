package com.ru.androidexperts.muzicapp.di

interface Module<T : ViewModelTag> {

    fun viewModel(): T
}