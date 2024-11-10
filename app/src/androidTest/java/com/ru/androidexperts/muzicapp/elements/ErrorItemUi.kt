package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.ru.androidexperts.muzicapp.ContainerAbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import com.ru.androidexperts.muzicapp.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher

interface ErrorItemUi : Enabled, Existence, Visibility {

    fun isEmptyResult()
    fun clickRetry()

    class Base(
        id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : ContainerAbstractUi(
        id = id,
        classType = LinearLayout::class.java,
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
        )
    ), ErrorItemUi {

        private val errorTextUi: DefaultTextUi = DefaultTextUi.Base(
            id = R.id.errorTextView,
            textColor = R.color.red,
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher,
        )

        private val retryButtonUi: ButtonUi = ButtonUi.Base(
            id = R.id.retryButton,
            textRes = R.string.retry,
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher,
        )

        override fun isEmptyResult() {
            errorTextUi.assertVisible()
            errorTextUi.assertText(R.string.no_songs_found)
            retryButtonUi.assertNotVisible()
        }

        override fun clickRetry() {
            retryButtonUi.click()
        }

        override fun assertVisible() {
            super.assertVisible()
            errorTextUi.assertVisible()
            errorTextUi.assertText(R.string.no_internet_connection)
            retryButtonUi.assertVisible()
            onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(0, R.id.errorItem))
                .check(matches(isDisplayed()))
        }
    }
}
