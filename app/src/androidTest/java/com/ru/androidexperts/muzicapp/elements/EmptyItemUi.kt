package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import com.ru.androidexperts.muzicapp.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher

interface EmptyItemUi : Enabled, Existence, Visibility {

    class Base(
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
            withId(R.id.emptyItem),
            isAssignableFrom(LinearLayout::class.java)
        )
    ), EmptyItemUi {

        private val emptyTextUi = DefaultTextUi.EmptyResult(
            containerIdMatcher = containerIdMatcher,
            containerClassTypeMatcher = containerClassTypeMatcher,
        )

        override fun assertVisible() {
            super.assertVisible()
            emptyTextUi.assertVisible()
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(0, R.id.emptyItem))
                .check(matches(isDisplayed()))
        }
    }
}