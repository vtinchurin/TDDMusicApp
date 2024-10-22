package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.Button
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import org.hamcrest.Matcher

interface ButtonUi : Enabled, Existence, Visibility {

    fun click()

    class Base(
        text: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
            withText(text),
            withId(R.id.retryButton),
            isAssignableFrom(Button::class.java)
        )
    ), ButtonUi {
        override fun click() {
            interaction.perform(androidx.test.espresso.action.ViewActions.click())
        }

    }
}