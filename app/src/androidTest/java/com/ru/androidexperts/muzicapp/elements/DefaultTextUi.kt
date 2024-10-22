package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import org.hamcrest.Matcher

interface DefaultTextUi : Enabled, Existence, Visibility {

    class EmptyResult(
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
            withId(R.id.emptyResultText),
            isAssignableFrom(TextView::class.java)
        )
    ), DefaultTextUi

    class Error(
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
            withId(R.id.errorText),
            isAssignableFrom(TextView::class.java)
        )
    ), DefaultTextUi
}
