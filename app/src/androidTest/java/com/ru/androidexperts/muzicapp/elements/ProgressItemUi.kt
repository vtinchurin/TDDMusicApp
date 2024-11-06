package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.FrameLayout
import com.ru.androidexperts.muzicapp.ContainerAbstractUi
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import org.hamcrest.Matcher

interface ProgressItemUi : Existence, Visibility {

    class Base(
        id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>
    ) : ContainerAbstractUi(
        id = id,
        classType = FrameLayout::class.java,
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
        )
    ), ProgressItemUi {

        private val progressUi = ProgressUi.Base(
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher,
        )

        override fun assertVisible() {
            super.assertVisible()
            progressUi.assertVisible()
        }
    }
}