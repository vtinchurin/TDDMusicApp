package com.ru.androidexperts.muzicapp.elements

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import com.ru.androidexperts.muzicapp.matchers.DrawableMatcher
import com.ru.androidexperts.muzicapp.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.matchers.waitTillDrawableChanged
import org.hamcrest.Matcher

interface TrackUi : Enabled, Existence, Visibility {

    class Base(
        private val id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) :
        AbstractUi(
            matchers = listOf(
                containerIdMatcher,
                containerClassTypeMatcher,
                withId(R.id.trackItem),
                isAssignableFrom(ConstraintLayout::class.java)
            )
        ), TrackUi {

        fun assertStopState() {
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(id, R.id.trackItem))
                .check(matches(DrawableMatcher(R.drawable.play)))
        }

        fun assertPlayState() {
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(id, R.id.trackItem))
                .check(matches(DrawableMatcher(R.drawable.pause)))
        }

        fun clickPlay() {
            onView(
                withId(R.id.playButton)
            ).perform(click())
        }

        fun waitTillStopped() {
            interaction.perform(waitTillDrawableChanged(R.drawable.play, 3000))
        }

    }
}
