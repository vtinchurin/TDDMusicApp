package com.ru.androidexperts.muzicapp.elements

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import com.ru.androidexperts.muzicapp.ContainerAbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.matchers.waitTillDisplayed
import org.hamcrest.Matcher

interface RecyclerViewUi {

    fun assertInitial()
    fun assertProgress()
    fun waitTillError()
    fun assertError()
    fun clickRetry()
    fun isEmpty()
    fun assertSuccess()
    fun clickFirstItemPlay()
    fun assertItemPlayState(index: Int)
    fun waitTillTrackStopped(index: Int)
    fun waitTillSuccess()

    class Base(
        private val id: Int,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : ContainerAbstractUi(
        id = id,
        classType = RecyclerView::class.java,
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher,
        ),
    ), RecyclerViewUi {

        private val errorItemUI: ErrorItemUi = ErrorItemUi.Base(
            id = R.id.errorItem,
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher,
        )

        private val progressUi: ProgressItemUi = ProgressItemUi.Base(
            id = R.id.progressItem,
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher,
        )

        private val itemList: List<TrackUi> = listOf(
            TrackUi.Base(
                index = 0,
                authorName = "Author 1",
                songName = "Song 1",
                containerIdMatcher = this.containerIdMatcher,
                containerClassTypeMatcher = this.containerClassTypeMatcher,
            ),
            TrackUi.Base(
                index = 1,
                authorName = "Author 2",
                songName = "Song 2",
                containerIdMatcher = this.containerIdMatcher,
                containerClassTypeMatcher = this.containerClassTypeMatcher,
            ),
        )

        override fun assertInitial() {
            onView(RecyclerViewMatcher(id).atPosition(0))
                .check(ViewAssertions.doesNotExist())
        }

        override fun assertProgress() {
            progressUi.assertVisible()
        }

        override fun waitTillError() {
            interaction.perform(waitTillDisplayed(R.id.errorItem, 3000))
        }

        override fun assertError() {
            errorItemUI.assertVisible()
        }

        override fun clickRetry() {
            errorItemUI.clickRetry()
        }

        override fun isEmpty() {
            errorItemUI.isEmptyResult()
        }

        override fun assertSuccess() {
            itemList.forEach { trackUi ->
                trackUi.assertInitialState()
            }
        }

        override fun clickFirstItemPlay() {
            itemList[0].clickPlay()
        }

        override fun assertItemPlayState(index: Int) {
            itemList[index].assertPlayState()
        }

        override fun waitTillTrackStopped(index: Int) {
            itemList[index].waitTillStopped()
        }

        override fun waitTillSuccess() {
            interaction.perform(waitTillDisplayed(R.id.trackItem, 3000))
            progressUi.doesNotExist()
        }
    }
}