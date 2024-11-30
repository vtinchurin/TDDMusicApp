package com.ru.androidexperts.muzicapp.ui.recycler.containers

import android.view.View
import android.widget.LinearLayout
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Id
import com.ru.androidexperts.muzicapp.core.Ui
import com.ru.androidexperts.muzicapp.core.assertions.Clickable
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.ui.recycler.defaults.DefaultText
import com.ru.androidexperts.muzicapp.ui.recycler.items.ImageButtonUi
import com.ru.androidexperts.muzicapp.ui.recycler.items.ImageUi
import org.hamcrest.Matcher

interface TrackUi : Id, Visibility, Clickable {

    fun assertPlayState()

    fun assertStopState()

    fun waitTillStopState()

    class Base(
        id: Int,
        position: Int,
        recyclerViewMatcher: RecyclerViewMatcher,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Ui.Recycler.Container(
        id,
        LinearLayout::class.java,
        position,
        recyclerViewMatcher,
        matchers
    ), TrackUi {

        private val songName: DefaultText = DefaultText.Base(
            id = R.id.songName,
            position = position,
            recyclerViewMatcher = recyclerViewMatcher
        )

        private val authorName: DefaultText = DefaultText.Base(
            id = R.id.authorName,
            position = position,
            recyclerViewMatcher = recyclerViewMatcher
        )

        private val trackImage: ImageUi = ImageUi.Base(
            id = R.id.imageView,
            recyclerViewMatcher = recyclerViewMatcher,
            position = position,
            matchers = parentMatchers()
        )

        private val playStopButton: ImageButtonUi = ImageButtonUi.Base(
            id = R.id.playButton,
            recyclerViewMatcher = recyclerViewMatcher,
            position = position,
            matchers = parentMatchers()
        )

        override fun assertVisible() {
            playStopButton.assertVisible()
            trackImage.assertVisible()
            authorName.assertVisible()
            songName.assertVisible()
        }

        override fun assertPlayState() {
            playStopButton.assertPlayState()
        }

        override fun assertStopState() {
            playStopButton.assertStopState()
        }

        override fun click() {
            playStopButton.click()
        }

        override fun waitTillStopState() {
            playStopButton.waitTillStop()
        }
    }
}