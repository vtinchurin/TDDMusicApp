package com.ru.androidexperts.muzicapp.ui.recycler.items

import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Clickable
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.DrawableMatcher
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.core.matchers.waitForView
import org.hamcrest.Matcher

interface ImageButtonUi : Clickable, Visibility {

    fun assertPlayState()
    fun assertStopState()
    fun waitTillStop()

    class Base(
        id: Int,
        position: Int,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Item(
        id,
        AppCompatImageButton::class.java,
        recyclerViewMatcher,
        position,
        matchers
    ), ImageButtonUi {

        override fun assertPlayState() {
            check(matches(DrawableMatcher(R.drawable.ic_stop)))
        }

        override fun assertStopState() {
            check(matches(DrawableMatcher(R.drawable.ic_play)))
        }

        override fun waitTillStop() {
            perform(
                waitForView(
                    id,
                    DrawableMatcher(R.drawable.ic_play),
                    5000
                )
            )
        }

        override fun click() {
            perform(ViewActions.click())
        }
    }
}
