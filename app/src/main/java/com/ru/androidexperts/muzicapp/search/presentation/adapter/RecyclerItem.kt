package com.ru.androidexperts.muzicapp.search.presentation.adapter

import com.ru.androidexperts.muzicapp.core.player.MusicPlayer
import com.ru.androidexperts.muzicapp.search.presentation.view.UpdateText
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.play.UpdatePlayStopButton
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUpdate

interface RecyclerItem {

    fun show(
        image: TrackImageUpdate,
        authorName: UpdateText,
        trackTitle: UpdateText,
        playStopBtn: UpdatePlayStopButton,
    ) = Unit

    fun show(errorMessage: UpdateText) = Unit

    fun playOrStop(actions: RecyclerActions.TogglePlayPause) = Unit

    fun changePlaying(): RecyclerItem = this

    fun type(): RecyclerItemType

    fun id(): String = this.javaClass.simpleName

    fun trackId(): Long = MusicPlayer.EMPTY_TRACK_ID

    fun isPlaying(): Boolean = false

    data class TrackUi(
        private val trackId: Long,
        private val coverUrl: TrackImageUiState,
        private val authorName: String,
        private val trackTitle: String,
        private val isPlaying: PlayStopUiState,
    ) : RecyclerItem {

        override fun show(
            image: TrackImageUpdate,
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
                actions.pause()
            else
                actions.play(this.trackId)
        }

        override fun trackId() = trackId

        override fun changePlaying(): RecyclerItem {
            return if (isPlaying is PlayStopUiState.Play)
                this.copy(coverUrl = coverUrl.changeState(), isPlaying = PlayStopUiState.Stop)
            else
                this.copy(coverUrl = coverUrl.changeState(), isPlaying = PlayStopUiState.Play)
        }

        override fun type() = RecyclerItemType.Track

        override fun isPlaying(): Boolean {
            return isPlaying is PlayStopUiState.Play
        }
    }

    object ProgressUi : RecyclerItem {
        override fun type() = RecyclerItemType.Progress
    }

    data class ErrorUi(private val resId: Int) : RecyclerItem {

        override fun type() = RecyclerItemType.Error

        override fun show(errorMessage: UpdateText) {
            errorMessage.update(resId)
        }
    }

    object NoTracksUi : RecyclerItem {

        override fun type(): RecyclerItemType = RecyclerItemType.NoTrack

    }
}