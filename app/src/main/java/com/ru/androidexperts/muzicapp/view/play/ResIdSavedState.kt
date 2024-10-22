package com.ru.androidexperts.muzicapp.view.play

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class ResIdSavedState : View.BaseSavedState {

    private var resId = 0

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        resId = parcelIn.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(resId)
    }

    fun restore(): Int = resId

    fun save(uiState: Int) {
        resId = uiState
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ResIdSavedState> {
        override fun createFromParcel(parcel: Parcel): ResIdSavedState =
            ResIdSavedState(parcel)

        override fun newArray(size: Int): Array<ResIdSavedState?> =
            arrayOfNulls(size)
    }
}