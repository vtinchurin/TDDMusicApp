package com.ru.androidexperts.muzicapp.ui.recycler.containers

import android.view.View
import android.widget.FrameLayout
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.ui.recycler.defaults.DefaultProgress
import org.hamcrest.Matcher

interface ProgressUi : Visibility {

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
    ), ProgressUi {

        private val progress: DefaultProgress = DefaultProgress.Base(
            id = R.id.progressBar,
            position,
            recyclerViewMatcher,
            parentMatchers()
        )

        override fun assertVisible() {
            super.assertVisible()
            progress.assertVisible()
        }
    }
}