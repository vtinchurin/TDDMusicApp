package com.ru.androidexperts.muzicapp.ui.standalone

import android.view.View
import android.widget.EditText
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import org.hamcrest.Matcher

interface EditTextUi : Visibility {

    fun addInput(text: String)

    class Base(
        id: Int,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Item(
        id = id,
        classType = EditText::class.java,
        matchers = matchers
    ), EditTextUi {

        override fun addInput(text: String) {
            interaction.perform(
                ViewActions.click(),
                clearText(),
                typeText(text),
                closeSoftKeyboard()
            )
        }
    }
}