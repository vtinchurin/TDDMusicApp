package com.ru.androidexperts.muzicapp.ui.recycler.defaults

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.test.espresso.action.ViewActions
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Clickable
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher

interface DefaultButton : Clickable, Visibility {

    class Base(
        id: Int,
        position: Int,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Item(
        id = id,
        classType = AppCompatButton::class.java,
        recyclerViewMatcher = recyclerViewMatcher,
        position = position,
        matchers = matchers
    ), DefaultButton {

        override fun click() {
            perform(ViewActions.click())
        }
    }
}
