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
        recyclerUi.clickFirstTrackPlayButton()
    }

    fun clickSecondTrackPlayButton() {
        recyclerUi.clickSecondTrackPlayButton()
    }

    fun assertFirstTrackPlayState() {
        recyclerUi.assertFirstTrackPlayState()
    }

    fun waitTillFirstTrackStopped() {
        recyclerUi.waitTillFirstTrackStopped()
    }

    fun assertSecondTrackPlayState() {
        recyclerUi.assertSecondTrackPlayState()
    }

    fun assertSecondTrackStopState() {
        recyclerUi.assertSecondTrackStopState()
    }

    fun waitTillSecondTrackStopped() {
        recyclerUi.waitTillSecondTrackStopped()
    }
}
