package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.AssertText
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import com.ru.androidexperts.muzicapp.matchers.TextColorMatcher
import org.hamcrest.Matcher

interface DefaultTextUi : Enabled, Existence, Visibility, AssertText {

    class Base(
        id: Int,
        textColor: Int = R.color.black,//TODO need to find out default color
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            withId(id),
            isAssignableFrom(TextView::class.java),
            TextColorMatcher(textColor),
            containerIdMatcher,
            containerClassTypeMatcher
        )
    ), DefaultTextUi
}
