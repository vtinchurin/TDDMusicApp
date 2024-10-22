package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.FrameLayout
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

interface ProgressUi : Enabled, Existence, Visibility {

    class Base(containerIdMatcher: Matcher<View>, containerClassTypeMatcher: Matcher<View>) :
        AbstractUi(
            matchers = listOf(
                containerIdMatcher,
                containerClassTypeMatcher,
                withId(R.id.progressItem),
                isAssignableFrom(FrameLayout::class.java)
            )
        ), ProgressUi {

        override fun assertVisible() {
            super.assertVisible()
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(0, R.id.progressItem))
                .check(matches(isDisplayed()))
        }
    }
}
