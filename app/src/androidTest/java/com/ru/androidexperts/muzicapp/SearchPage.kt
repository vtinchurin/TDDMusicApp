package com.ru.androidexperts.muzicapp

import android.widget.LinearLayout
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.ui.standalone.InputUi
import com.ru.androidexperts.muzicapp.ui.standalone.RecyclerUi

class SearchPage : Ui.Container(
    id = R.id.rootLayout,
    classType = LinearLayout::class.java
) {

    private val inputUi: InputUi = InputUi(
        matchers = parentMatchers()
    )

    private val recyclerUi: RecyclerUi = RecyclerUi.Base(
        id = R.id.recyclerView,
        matchers = parentMatchers()
    )

    fun assertInitialState() {
        inputUi.assertVisible()
        recyclerUi.assertVisible()
        recyclerUi.assertInitialState()
    }

    fun addUserInput(text: String) {
        inputUi.addInput(text = text)
    }

    fun assertProgressState() {
        recyclerUi.assertProgressState()
    }

    fun waitTillError() {
        recyclerUi.waitTillError()
    }

    fun assertErrorState() {
        recyclerUi.assertErrorState()
    }

    fun clickRetry() {
        recyclerUi.clickRetry()
    }

    fun waitTillNoTracksResponse() {
        recyclerUi.waitTillNoTracksResponse()
    }

    fun assertEmptyState() {
        recyclerUi.assertEmptyState()
    }

    fun waitTillSuccessResponse() {
        recyclerUi.waitTillSuccessResponse()
    }

    fun assertSuccessState() {
        recyclerUi.assertSuccessState()
    }

    fun clickFirstTrackPlayButton() {
        recyclerUi.clickTrackPlayButton(position = 0)
    }

    fun clickSecondTrackPlayButton() {
        recyclerUi.clickTrackPlayButton(position = 1)
    }

    fun assertFirstTrackPlayState() {
        recyclerUi.assertTrackPlayState(position = 0)
    }

    fun waitTillFirstTrackStopped() {
        recyclerUi.waitTillTrackStopped(position = 0)
    }

    fun assertSecondTrackPlayState() {
        recyclerUi.assertTrackPlayState(position = 1)
    }

    fun assertSecondTrackStopState() {
        recyclerUi.assertTrackStopState(position = 1)
    }

    fun waitTillSecondTrackStopped() {
        recyclerUi.waitTillTrackStopped(position = 1)
    }

    fun assertThirdTrackPlayState() {
        recyclerUi.assertTrackPlayState(position = 2)
    }

    fun waitTillThirdTrackStopped() {
        recyclerUi.waitTillTrackStopped(position = 2)
    }

    fun clickThirdTrackPlayButton() {
        recyclerUi.clickTrackPlayButton(position = 2)
    }
}
