package com.ru.androidexperts.muzicapp.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {

    fun show(containerId: Int, fragmentManager: FragmentManager) = Unit

    abstract class Replace(private val fragmentClass: Class<out Fragment>) : Screen {

        override fun show(containerId: Int, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                .replace(containerId, newFragment())
                .commit()
        }

        protected open fun newFragment(): Fragment =
            fragmentClass.getDeclaredConstructor().newInstance()
    }

    object Empty : Screen
}