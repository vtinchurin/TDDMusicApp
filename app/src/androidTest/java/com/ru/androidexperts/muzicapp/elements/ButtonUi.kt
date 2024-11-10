package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Clickable
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import com.ru.androidexperts.muzicapp.matchers.DrawableMatcher
import com.ru.androidexperts.muzicapp.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.matchers.waitTillDrawableChanged
import org.hamcrest.Matcher

interface ButtonUi : Enabled, Existence, Visibility, Clickable {

    class Base(
        id: Int,
        textRes: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            withId(id),
            withText(textRes),
            isAssignableFrom(Button::class.java),
            containerIdMatcher,
            containerClassTypeMatcher
        )
    ), ButtonUi

    class Image(
        private val id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            withId(id),
            isAssignableFrom(ImageButton::class.java),
            containerIdMatcher,
            containerClassTypeMatcher
        )
    ), ButtonUi {

        fun assertPlayState(index: Int) {
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(index, id))
                .check(matches(DrawableMatcher(R.drawable.ic_stop)))
        }

        fun assertStopState(index: Int) {
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(index, id))
                .check(matches(DrawableMatcher(R.drawable.ic_play)))
        }

        fun waitTillStopped() {
            interaction.perform(waitTillDrawableChanged(R.drawable.ic_play, 3000))
        }
    }
}