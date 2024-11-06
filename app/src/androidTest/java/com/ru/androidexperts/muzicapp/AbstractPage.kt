package com.ru.androidexperts.muzicapp

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher

abstract class AbstractPage(
    containerId: Int,
    containerClassType: Class<out View>,
) {
    protected val containerIdMatcher: Matcher<View> = withParent(withId(containerId))
    protected val containerClassTypeMatcher: Matcher<View> =
        withParent(isAssignableFrom(containerClassType))
}