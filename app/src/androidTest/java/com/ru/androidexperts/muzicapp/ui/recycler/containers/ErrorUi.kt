package com.ru.androidexperts.muzicapp.ui.recycler.containers

import android.view.View
import android.widget.LinearLayout
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Clickable
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.ui.recycler.defaults.DefaultButton
import com.ru.androidexperts.muzicapp.ui.recycler.defaults.DefaultText
import org.hamcrest.Matcher

interface ErrorUi : Visibility, Clickable {

    class Base(
        id: Int,
        position: Int = 0,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Container(
        id = id,
        classType = LinearLayout::class.java,
        position = position,
        recyclerViewMatcher = recyclerViewMatcher,
        matchers = matchers
    ), ErrorUi {

        private val errorText = DefaultText.Base(
            id = R.id.errorTextView,
            position = position,
            recyclerViewMatcher = recyclerViewMatcher,
            matchers = parentMatchers()
        )
        private val retryButton = DefaultButton.Base(
            id = R.id.retryButton,
            position = position,
            recyclerViewMatcher = recyclerViewMatcher,
            matchers = parentMatchers()
        )

        override fun assertVisible() {
            super.assertVisible()
            errorText.assertVisible()
            retryButton.assertVisible()
            errorText.assertText(R.string.no_internet_connection)
        }

        override fun click() {
            retryButton.click()
        }
    }
}