package com.ru.androidexperts.muzicapp.core.assertions

interface Visibility {

    fun assertVisible()

    fun assertNotVisible()

    fun waitTillDisplayed(timeout: Long)
}