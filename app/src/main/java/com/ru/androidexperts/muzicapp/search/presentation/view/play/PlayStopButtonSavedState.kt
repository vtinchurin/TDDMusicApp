package com.ru.androidexperts.muzicapp.search.presentation.view.play

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View

@Suppress("DEPRECATION")
class PlayStopButtonSavedState : View.BaseSavedState {

    private lateinit var state: PlayStopUiState

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcelIn.readSerializable(
                PlayStopUiState::class.java.classLoader,
                PlayStopUiState::class.java
            ) as PlayStopUiState
        } else {
            parcelIn.readSerializable() as PlayStopUiState
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeSerializable(state)
    }

    fun restore(): PlayStopUiState = state

    fun save(uiState: PlayStopUiState) {
        state = uiState
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<PlayStopButtonSavedState> {
        override fun createFromParcel(parcel: Parcel): PlayStopButtonSavedState =
            PlayStopButtonSavedState(parcel)

        override fun newArray(size: Int): Array<PlayStopButtonSavedState?> =
            arrayOfNulls(size)
    }
}