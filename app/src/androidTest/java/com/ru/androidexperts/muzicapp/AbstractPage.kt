package com.ru.androidexperts.muzicapp

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher

abstract class AbstractPage(
    containerId: Int,
    classType: Class<out View>,
) {
    protected val containerIdMatcher: Matcher<View> = withParent(withId(containerId))
    protected val classTypeMatcher: Matcher<View> =
        withParent(isAssignableFrom(classType))
}