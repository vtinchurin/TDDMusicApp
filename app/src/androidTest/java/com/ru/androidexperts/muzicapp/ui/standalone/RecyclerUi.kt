package com.ru.androidexperts.muzicapp.ui.standalone

import android.view.View
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.ui.recycler.containers.ErrorUi
import com.ru.androidexperts.muzicapp.ui.recycler.containers.NoSongsUi
import com.ru.androidexperts.muzicapp.ui.recycler.containers.ProgressUi
import com.ru.androidexperts.muzicapp.ui.recycler.containers.TrackUi
import org.hamcrest.Matcher

interface RecyclerUi : Visibility {

    fun assertInitialState()
    fun assertProgressState()
    fun assertErrorState()
    fun assertEmptyState()
    fun assertSuccessState()
    fun assertFirstTrackPlayState()
    fun assertSecondTrackPlayState()
    fun waitTillError()
    fun waitTillNoTracksResponse()
    fun waitTillSuccessResponse()
    fun waitTillFirstTrackStopped()
    fun waitTillSecondTrackStopped()
    fun clickRetry()
    fun clickFirstTrackPlayButton()
    fun clickSecondTrackPlayButton()

    class Base(
        id: Int,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Base(id, matchers), RecyclerUi {

        private val error: ErrorUi = ErrorUi.Base(
            id = R.id.errorItem,
            recyclerViewMatcher = recyclerViewMatcher(),
        )

        private val noTracks: NoSongsUi = NoSongsUi.Base(
            id = R.id.noSongsItem,
            recyclerViewMatcher = recyclerViewMatcher(),
        )

        private val progress: ProgressUi = ProgressUi.Base(
            id = R.id.progressItem,
            recyclerViewMatcher = recyclerViewMatcher(),
        )

        private val tracks: List<TrackUi> = listOf(
            TrackUi.Base(
                id = R.id.trackItem,
                position = 0,
                recyclerViewMatcher = recyclerViewMatcher()
            ),
            TrackUi.Base(
                id = R.id.trackItem,
                position = 1,
                recyclerViewMatcher = recyclerViewMatcher()
            )
        )

        override fun assertInitialState() {
            hasNoItems()
        }

        override fun assertProgressState() {
            hasItemCount(1)
            progress.assertVisible()
        }

        override fun waitTillError() {
            error.waitTillDisplayed(3000)
        }

        override fun assertErrorState() {
            hasItemCount(1)
            error.assertVisible()
        }

        override fun clickRetry() {
            error.click()
        }

        override fun waitTillNoTracksResponse() {
            noTracks.waitTillDisplayed(3000)
        }

        override fun assertEmptyState() {
            hasItemCount(1)
            noTracks.assertVisible()
        }

        override fun waitTillSuccessResponse() {
            tracks.forEach {
                it.waitTillDisplayed(3000)
            }
        }

        override fun assertSuccessState() {
            hasItemCount(2)
            tracks.forEach { trackUi ->
                trackUi.assertVisible()
                trackUi.assertStopState()
            }
        }

        override fun clickFirstTrackPlayButton() {
            tracks[0].click()
        }

        override fun clickSecondTrackPlayButton() {
            tracks[1].click()
        }

        override fun assertFirstTrackPlayState() {
            tracks[0].assertPlayState()
            tracks[1].assertStopState()
        }

        override fun assertSecondTrackPlayState() {
            tracks[0].assertStopState()
            tracks[1].assertPlayState()
        }

        override fun waitTillFirstTrackStopped() {
            tracks[1].assertStopState()
            tracks[0].waitTillStopState()
        }

        override fun waitTillSecondTrackStopped() {
            tracks[0].assertStopState()
            tracks[1].waitTillStopState()
        }
    }
}