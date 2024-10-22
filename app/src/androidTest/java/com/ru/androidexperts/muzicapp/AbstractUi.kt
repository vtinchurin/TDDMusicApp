package com.ru.androidexperts.muzicapp

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ru.androidexperts.muzicapp.assertions.AssertText
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

/**
 *
 * Абстрактный класс имеющий все основное поведение
 *
 */
abstract class AbstractUi(
    matchers: List<Matcher<View>>,
) : Visibility, Existence, Enabled, AssertText {
    protected val interaction: ViewInteraction = onView(
        allOf(matchers)
    )

    override fun assertVisible() {
        interaction.check(matches(isDisplayed()))
    }

    override fun assertNotVisible() {
        interaction.check(matches(not(isDisplayed())))
    }

    override fun doesNotExist() {
        interaction.check(ViewAssertions.doesNotExist())
    }

    override fun assertEnabled() {
        interaction.check(matches(isEnabled()))
    }

    override fun assertNotEnabled() {
        interaction.check(matches(isNotEnabled()))
    }

    override fun assertText(text: String) {
        interaction.check(matches(withText(text)))
    }
}