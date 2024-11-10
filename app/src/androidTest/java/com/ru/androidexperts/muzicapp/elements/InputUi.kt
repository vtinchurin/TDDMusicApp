package com.ru.androidexperts.muzicapp.elements

import android.view.View
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.textfield.TextInputLayout
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.ContainerAbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Visibility
import org.hamcrest.Matcher

interface InputUi : Visibility {

    fun addInput(text: String)

    fun assertInitial()

    class Layout(
        id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>
    ) : ContainerAbstractUi(
        id = id,
        classType = TextInputLayout::class.java,
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
        )
    ), InputUi {

        private val inputInteraction: EditText =
            EditText(
                id = R.id.inputEditText,
                containerIdMatcher = this.containerIdMatcher,
                containerClassTypeMatcher = this.containerClassTypeMatcher,
            )

        override fun addInput(text: String) {
            inputInteraction.addInput(text)
        }

        override fun assertInitial() {
            interaction.check(matches(isDisplayed()))
            inputInteraction.assertInitial()
        }
    }

    class EditText(
        id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>
    ) : AbstractUi(
        matchers = listOf(
            withId(id),
            isAssignableFrom(android.widget.EditText::class.java),
            containerIdMatcher,
            containerClassTypeMatcher
        ),
    ), InputUi {

        override fun addInput(text: String) {
            interaction.perform(ViewActions.click())
            interaction.perform(typeText(text))
            interaction.perform(closeSoftKeyboard())
        }

        override fun assertInitial() {
            interaction.check(matches(withText("")))
        }
    }
}