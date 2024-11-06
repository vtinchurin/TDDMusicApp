package com.ru.androidexperts.muzicapp.adapter

import com.ru.androidexperts.muzicapp.view.UpdateText
import com.ru.androidexperts.muzicapp.view.image.UpdateImageUrl
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.view.play.UpdatePlayStopButton

interface RecyclerItem {

    fun show(
        image: UpdateImageUrl,
        authorName: UpdateText,
        trackTitle: UpdateText,
        playStopBtn: UpdatePlayStopButton,
    ) = Unit

    fun show(errorMessage: UpdateText) = Unit

    fun playOrStop(actions: RecyclerActions.TogglePlayPause) = Unit

    fun changePlaying(): RecyclerItem = this

    fun type(): RecyclerItemType

    fun id(): String = this.javaClass.simpleName

    fun trackId(): Long = -1L

    data class TrackUi(
        private val trackId: Long,
        private val coverUrl: String,
        private val authorName: String,
        private val trackTitle: String,
        private val isPlaying: PlayStopUiState,
    ) : RecyclerItem {

        override fun show(
            image: UpdateImageUrl,
            authorName: UpdateText,
            trackTitle: UpdateText,
            playStopBtn: UpdatePlayStopButton,
        ) {
            image.update(coverUrl)
            authorName.update(this.authorName)
            trackTitle.update(this.trackTitle)
            playStopBtn.update(isPlaying)
        }

        override fun playOrStop(actions: RecyclerActions.TogglePlayPause) {
            if (isPlaying is PlayStopUiState.Play)
                actions.stop()
            else
                actions.play(this.trackId)
        }

        override fun trackId() = trackId

        override fun changePlaying(): RecyclerItem {
            return if (isPlaying is PlayStopUiState.Play)
                this.copy(isPlaying = PlayStopUiState.Stop)
            else
                this.copy(isPlaying = PlayStopUiState.Play)
        }

        override fun type() = RecyclerItemType.Track

    }

    object ProgressUi : RecyclerItem {
        override fun type() = RecyclerItemType.Progress
    }

    data class ErrorUi(private val message: String) : RecyclerItem {

        override fun type() = RecyclerItemType.Error

        override fun show(errorMessage: UpdateText) {
            errorMessage.update(message)
        }
    }

    object NoTracksUi : RecyclerItem {

        override fun type(): RecyclerItemType = RecyclerItemType.NoTrack

    }
}