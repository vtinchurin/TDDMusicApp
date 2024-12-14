package com.ru.androidexperts.muzicapp.ui.recycler.items

import android.view.View
import android.widget.ImageView
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher

interface ImageUi : Visibility {

    class Base(
        id: Int,
        position: Int,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Item(
        id = id,
        classType = ImageView::class.java,
        recyclerViewMatcher = recyclerViewMatcher,
        position = position,
        matchers = matchers
    ), ImageUi
}
