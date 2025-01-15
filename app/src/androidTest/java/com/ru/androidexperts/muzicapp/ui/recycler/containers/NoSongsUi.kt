package com.ru.androidexperts.muzicapp.ui.recycler.containers

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

interface NoSongsUi : Visibility {

    class Base(
        id: Int,
        position: Int = 0,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Container(
        id = id,
        classType = FrameLayout::class.java,
        position = position,
        recyclerViewMatcher = recyclerViewMatcher,
        matchers = matchers
    ), NoSongsUi {

        override fun assertVisible() {
            super.assertVisible()
            val noTracksTextMatcher = withChild(
                allOf(
                    isAssignableFrom(TextView::class.java),
                    withText(R.string.no_songs_found)
                )
            )
            check(matches(noTracksTextMatcher))
        }
    }
}
