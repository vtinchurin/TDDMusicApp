package com.ru.androidexperts.muzicapp.view.trackImage

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View

class TrackImageSavedState : View.BaseSavedState {

    private lateinit var state: TrackImageUiState

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel, loader: ClassLoader) : super(parcelIn, loader) {
        state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcelIn.readSerializable(
                TrackImageUiState::class.java.classLoader,
                TrackImageUiState::class.java
            ) as TrackImageUiState
        } else {
            parcelIn.readSerializable() as TrackImageUiState
        }
    }

    private constructor(parcelIn: Parcel) : super(parcelIn)

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeSerializable(state)
    }

    fun restore(): TrackImageUiState = state

    fun save(uiState: TrackImageUiState) {
        state = uiState
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.ClassLoaderCreator<TrackImageSavedState> {
        override fun createFromParcel(source: Parcel, loader: ClassLoader): TrackImageSavedState =
            TrackImageSavedState(source, loader)

        override fun createFromParcel(parcel: Parcel): TrackImageSavedState =
            TrackImageSavedState(parcel)


        override fun newArray(size: Int): Array<TrackImageSavedState?> =
            arrayOfNulls(size)
    }

}