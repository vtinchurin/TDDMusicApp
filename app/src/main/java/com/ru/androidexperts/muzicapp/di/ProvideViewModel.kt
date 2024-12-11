package com.ru.androidexperts.muzicapp.di

import com.ru.androidexperts.muzicapp.search.di.ProvideSearchViewModel

interface ProvideViewModel {

    fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T

    class Make(core: Core) : ProvideViewModel {

        private var chain: ProvideViewModel

        init {
            chain = Error()
            chain = ProvideSearchViewModel(core, chain)
        }

        override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T =
            chain.makeViewModel(clasz)
    }

    class Error : ProvideViewModel {

        override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T {
            throw IllegalStateException("unknown class $clasz")
        }
    }
}