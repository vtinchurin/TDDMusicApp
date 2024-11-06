package com.ru.androidexperts.muzicapp.elements

import android.view.View
import android.widget.LinearLayout
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.ru.androidexperts.muzicapp.ContainerAbstractUi
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.assertions.Enabled
import com.ru.androidexperts.muzicapp.assertions.Existence
import com.ru.androidexperts.muzicapp.assertions.Visibility
import org.hamcrest.Matcher

interface TrackUi : Enabled, Existence, Visibility {

    fun assertInitialState()
    fun assertStopState()
    fun assertPlayState()
    fun clickPlay()
    fun waitTillStopped()

    class Base(
        private val index: Int,
        private val authorName: String,
        private val songName: String,
        containerIdMatcher: Matcher<View>,
        containerClassTypeMatcher: Matcher<View>,
    ) : ContainerAbstractUi(
        id = R.id.trackItem,
        classType = LinearLayout::class.java,
        matchers = listOf(
            containerIdMatcher,
            containerClassTypeMatcher
        )
    ), TrackUi {

        private val playStopButtonUi: ButtonUi.Image = ButtonUi.Image(
            id = R.id.playButton,
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher,
        )

        private val authorNameTextUi: DefaultTextUi = DefaultTextUi.Base(
            id = R.id.authorName,
            containerIdMatcher = withParent(containerIdMatcher),
            containerClassTypeMatcher = withParent(containerIdMatcher)
        )

        private val songNameTextUi: DefaultTextUi = DefaultTextUi.Base(
            id = R.id.songName,
            containerIdMatcher = withParent(containerIdMatcher),
            containerClassTypeMatcher = withParent(containerIdMatcher)
        )

        private val imageViewUi: ImageViewUi = ImageViewUi.Base(
            id = R.id.imageView,
            drawableResId = R.drawable.ic_artwork,
            containerIdMatcher = this.containerIdMatcher,
            containerClassTypeMatcher = this.containerClassTypeMatcher
        )

        override fun assertInitialState() {
            authorNameTextUi.assertText(text = authorName)
            songNameTextUi.assertText(text = songName)
            imageViewUi.assertVisible()
            assertStopState()
        }

        override fun assertStopState() {
            playStopButtonUi.assertStopState(index = index)
        }

        override fun assertPlayState() {
            playStopButtonUi.assertPlayState(index = index)
        }

        override fun clickPlay() {
            playStopButtonUi.click()
        }

        override fun waitTillStopped() {
            playStopButtonUi.waitTillStopped()
        }
    }
}