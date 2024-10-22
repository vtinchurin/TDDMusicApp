package com.ru.androidexperts.muzicapp

import android.widget.LinearLayout
import com.ru.androidexperts.muzicapp.elements.InputUi
import com.ru.androidexperts.muzicapp.elements.RecyclerViewUi

class AppPage : AbstractPage(containerId = R.id.rootLayout, classType = LinearLayout::class.java) {

    private val inputUi = InputUi(
        containerIdMatcher = containerIdMatcher,
        containerClassTypeMatcher = classTypeMatcher,
    )


    private val recyclerViewUi = RecyclerViewUi(
        containerIdMatcher = containerIdMatcher,
        containerClassTypeMatcher = classTypeMatcher,
    )

    fun assertInitialState() {
        inputUi.assertInitial()
        recyclerViewUi.assertInitial()
    }

    fun addUserInput(text: String) {
        inputUi.addInput(text = text)
    }

    fun assertProgressState() {
        recyclerViewUi.assertProgress()
    }

    fun waitTillError() {
        recyclerViewUi.waitTillError()

    }

    fun assertErrorState() {
        recyclerViewUi.assertError()
    }

    fun clickRetry() {
        recyclerViewUi.clickRetry()
    }

    fun waitTillSuccessResponse() {
        recyclerViewUi.waitTillSuccess()
    }

    fun assertEmptyState() {
        recyclerViewUi.isEmpty()
    }

    fun assertSuccessState() {
        recyclerViewUi.assertSuccess()

    }

    fun clickFirstTrackPlayButton() {
        recyclerViewUi.clickFirstItemPlay()
    }

    fun assertFirstTrackPlayState() {
        recyclerViewUi.assertItemPlayState(index = 0)
    }

    fun waitTillFirstTrackStopped() {
        recyclerViewUi.waitTillTrackStopped(index = 0)
    }

    fun assertSecondTrackPlayState() {
        recyclerViewUi.assertItemPlayState(index = 1)
    }

    fun waitTillSecondTrackStopped() {
        recyclerViewUi.waitTillTrackStopped(index = 1)
    }
}
