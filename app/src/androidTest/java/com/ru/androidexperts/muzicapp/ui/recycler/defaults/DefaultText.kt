package com.ru.androidexperts.muzicapp.ui.recycler.defaults

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.AssertText
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher

interface DefaultText : Visibility, AssertText {

    class Base(
        id: Int,
        position: Int,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Item(
        id = id,
        classType = AppCompatTextView::class.java,
        recyclerViewMatcher = recyclerViewMatcher,
        position = position,
        matchers = matchers
    ), DefaultText {

        override fun assertText(text: String) {
            check(matches(withText(text)))
        }

        override fun assertText(stringResId: Int) {
            check(matches(withText(stringResId)))
        }
    }
}