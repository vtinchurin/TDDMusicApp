package com.ru.androidexperts.muzicapp.elements

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ru.androidexperts.muzicapp.AbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.matchers.waitTillDisplayed
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

class RecyclerViewUi(
    containerIdMatcher: Matcher<View>,
    containerClassTypeMatcher: Matcher<View>,
) : AbstractUi(
    matchers = listOf(
        containerIdMatcher,
        containerClassTypeMatcher,
        withId(R.id.recyclerView),
        isAssignableFrom(RecyclerView::class.java)
    )
) {


    private val errorItemUI = ErrorItemUi.Base(
        containerIdMatcher = containerIdMatcher,
        containerClassTypeMatcher = containerClassTypeMatcher,
    )
    private val emptyItemUI = EmptyItemUi.Base(
        containerIdMatcher = containerIdMatcher,
        containerClassTypeMatcher = containerClassTypeMatcher,
    )
    private val progressUi = ProgressUi.Base(
        containerIdMatcher = containerIdMatcher,
        containerClassTypeMatcher = containerClassTypeMatcher,
    )
    private val itemList = listOf(
        TrackUi.Base(
            id = 0,
            containerIdMatcher = containerIdMatcher,
            containerClassTypeMatcher = containerClassTypeMatcher,
        ),
        TrackUi.Base(
            id = 1,
            containerIdMatcher = containerIdMatcher,
            containerClassTypeMatcher = containerClassTypeMatcher,
        ),
    )

    fun assertInitial() {
        interaction.check(matches(not(isDisplayed())))
    }

    fun assertProgress() {
        progressUi.assertVisible()
    }

    fun waitTillError() {
        interaction.perform(waitTillDisplayed(R.id.errorItem, 3000))
        // onView(isRoot()).perform(waitTillDisplayed(R.id.errorItem, 3000))
    }

    fun assertError() {
        errorItemUI.assertVisible()
//        onView(RecyclerViewMatcher(R.id.recyclerView).atPosition(0, R.id.errorItem))
//            .check(matches(isDisplayed()))
    }

    fun clickRetry() {
        errorItemUI.clickRetry()
    }

    fun isEmpty() {
        emptyItemUI.assertVisible()
    }

    fun assertSuccess() {
        itemList.forEach {
            it.assertStopState()
        }
    }

    fun clickFirstItemPlay() {
        itemList[0].clickPlay()
    }

    fun assertItemPlayState(index: Int) {
        itemList[index].assertPlayState()
    }

    fun waitTillTrackStopped(index: Int) {
        itemList[index].waitTillStopped()
    }

    fun waitTillSuccess() {
        interaction.perform(waitTillDisplayed(R.id.trackItem, 3000))
        progressUi.doesNotExist()
    }


}
