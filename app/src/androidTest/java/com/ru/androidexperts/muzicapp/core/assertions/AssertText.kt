package com.ru.androidexperts.muzicapp.core.assertions

interface AssertText {

    fun assertText(text: String)

    fun assertText(stringResId: Int)
}