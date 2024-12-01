package com.ru.androidexperts.muzicapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ru.androidexperts.muzicapp.di.ProvideViewModel
import com.ru.androidexperts.muzicapp.di.ViewModelTag
import com.ru.androidexperts.muzicapp.core.Navigation
import com.ru.androidexperts.muzicapp.core.Screen
import com.ru.androidexperts.muzicapp.search.SearchScreen

class MainActivity : AppCompatActivity(), ProvideViewModel, Navigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            navigate(SearchScreen)
    }

    override fun navigate(screen: Screen) {
        screen.show(R.id.container, supportFragmentManager)
    }

    override fun <T : ViewModelTag> makeViewModel(clasz: Class<T>): T {
        return (application as ProvideViewModel).makeViewModel(clasz)
    }
}