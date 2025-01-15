package com.ru.androidexperts.muzicapp.search.uiObservable.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.presentation.view.UpdateText

interface FakeUpdateText : UpdateText {

    fun assertUpdateCalled(expectedText: String)

    class Base(private val order: Order) : FakeUpdateText {

        override fun assertUpdateCalled(expectedText: String) {
            order.assert(SET_TEXT, expectedText)
        }

        override fun update(newText: String) {
            order.add(SET_TEXT, newText)
        }

        override fun update(textResId: Int) = Unit

        companion object {
            private const val SET_TEXT = "UpdateText#update"
        }

    }
}