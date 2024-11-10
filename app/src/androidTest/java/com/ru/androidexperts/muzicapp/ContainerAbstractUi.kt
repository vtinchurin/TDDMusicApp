package com.ru.androidexperts.muzicapp

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher

abstract class ContainerAbstractUi(
    id: Int,
    classType: Class<out View>,
    matchers: List<Matcher<View>>
) : AbstractUi(
    matchers = matchers + withId(id) + isAssignableFrom(classType)
) {
    protected val containerIdMatcher: Matcher<View> = withParent(withId(id))
    protected val containerClassTypeMatcher: Matcher<View> = withParent(isAssignableFrom(classType))
}