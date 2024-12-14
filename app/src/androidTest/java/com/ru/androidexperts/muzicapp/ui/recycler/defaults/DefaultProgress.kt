package com.ru.androidexperts.muzicapp.ui.recycler.defaults

import android.view.View
import android.widget.ProgressBar
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher

interface DefaultProgress : Visibility {

    class Base(
        id: Int,
        position: Int,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Item(
        id = id,
        classType = ProgressBar::class.java,
        recyclerViewMatcher = recyclerViewMatcher,
        position = position,
        matchers = matchers
    ), DefaultProgress
}