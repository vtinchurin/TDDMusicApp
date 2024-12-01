package com.ru.androidexperts.muzicapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ru.androidexperts.muzicapp.core.Navigation
import com.ru.androidexperts.muzicapp.core.Screen
import com.ru.androidexperts.muzicapp.presentation.SearchFragment
import com.ru.androidexperts.muzicapp.search.SearchScreen

class MainActivity : AppCompatActivity(),Navigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            navigate(SearchScreen)
    }

    override fun navigate(screen: Screen) {
        screen.show(R.id.container,supportFragmentManager)
    }
}