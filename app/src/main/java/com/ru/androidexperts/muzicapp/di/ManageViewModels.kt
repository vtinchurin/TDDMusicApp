package com.ru.androidexperts.muzicapp.di

interface ManageViewModels : ProvideViewModel {

    class Factory(
        private val make: ProvideViewModel,
    ) : ManageViewModels {

        private val viewModelsMap = mutableMapOf<Class<out ViewModelTag>, ViewModelTag?>()

        override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T =
            if (viewModelsMap[clasz] == null) {
                val viewModel = make.makeViewModel(clasz)
                viewModelsMap[clasz] = viewModel
                viewModel
            } else viewModelsMap[clasz] as T
    }
}