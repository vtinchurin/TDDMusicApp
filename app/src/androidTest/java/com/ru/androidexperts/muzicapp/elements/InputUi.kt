package com.ru.androidexperts.muzicapp.elements

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.textfield.TextInputEditText
import com.ru.androidexperts.muzicapp.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

/**
 * Тут я так и не придумал как сделать лучше
 */

class InputUi(containerIdMatcher: Matcher<View>, containerClassTypeMatcher: Matcher<View>) {

    private val inputInteraction: ViewInteraction = onView(
        allOf(
            isAssignableFrom(TextInputEditText::class.java),
            withId(R.id.inputEditText),
        )
    )

    fun addInput(text: String) {
        inputInteraction.perform(click())
        inputInteraction.perform(replaceText(text))
        inputInteraction.perform(closeSoftKeyboard())
    }

    fun assertInitial() {
        inputInteraction.check(matches(withText("")))
    }

}
