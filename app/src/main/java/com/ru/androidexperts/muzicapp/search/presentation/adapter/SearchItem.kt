package com.ru.androidexperts.muzicapp.search.presentation.adapter

import com.ru.androidexperts.muzicapp.core.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.core.adapter.RecyclerItemType
import com.ru.androidexperts.muzicapp.search.presentation.view.UpdateText
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.play.UpdatePlayStopButton
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUpdate

interface SearchItem : RecyclerItem {

    interface Track : SearchItem {

        fun show(
            image: TrackImageUpdate,
            authorName: UpdateText,
            trackTitle: UpdateText,
            playStopBtn: UpdatePlayStopButton,
        )

        fun trackId(): Long

        fun playOrStop(actions: SearchScreenActions.TogglePlayPause)

        fun changePlaying(): Track
    }

    interface Error : SearchItem {
        fun show(errorMessage: UpdateText)
    }

    data class TrackUi(
        private val trackId: Long,
        private val coverUrl: TrackImageUiState,
        private val authorName: String,
        private val trackTitle: String,
        private val isPlaying: PlayStopUiState,
    ) : Track {

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

        override fun changePlaying(): Track {
            return if (isPlaying.isPlaying())
                this.copy(
                    coverUrl = coverUrl.changeState(),
                    isPlaying = PlayStopUiState.Stop
                )
            else
                this.copy(
                    coverUrl = coverUrl.changeState(),
                    isPlaying = PlayStopUiState.Play
                )
        }

        override fun playOrStop(actions: SearchScreenActions.TogglePlayPause) {
            if (isPlaying.isPlaying())
                actions.pause()
            else
                actions.play(trackId)
        }

        override fun trackId() = trackId

        override fun type() = SearchItemType.Track

    }

    data class ErrorUi(private val resId: Int) : Error {

        override fun type() = SearchItemType.Error

        override fun show(errorMessage: UpdateText) {
            errorMessage.update(resId)
        }

    }

    object ProgressUi : SearchItem {
        override fun type() = SearchItemType.Progress
    }

    object NoTracksUi : SearchItem {
        override fun type(): RecyclerItemType = SearchItemType.NoTrack
    }
}