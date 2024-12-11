package com.ru.androidexperts.muzicapp.core

import com.ru.androidexperts.muzicapp.search.NavigateToSearch
import com.ru.androidexperts.muzicapp.search.SearchScreen

interface Navigation : NavigateToSearch {

    fun navigate(screen:Screen)

    override fun navigateToSearch() = navigate(SearchScreen)
}