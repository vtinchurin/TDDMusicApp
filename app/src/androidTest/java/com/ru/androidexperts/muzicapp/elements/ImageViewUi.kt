package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import com.ru.androidexperts.muzicapp.matchers.DrawableMatcher
import org.hamcrest.Matcher

interface ImageViewUi : Existence, Visibility {

    class Base(
        id: Int,
        drawableResId: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : AbstractUi(
        matchers = listOf(
            withId(id),
            isAssignableFrom(ImageView::class.java),
            DrawableMatcher(drawableResId),
            containerIdMatcher,
            containerClassTypeMatcher,
        )
    ), ImageViewUi
}