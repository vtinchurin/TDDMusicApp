package com.ru.androidexperts.muzicapp.view.image

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class UrlImageSavedState : View.BaseSavedState {

    private var url = ""

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        url = parcelIn.readString() ?: ""
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeString(url)
    }

    fun restore(): String = url

    fun save(uiState: String) {
        url = uiState
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<UrlImageSavedState> {
        override fun createFromParcel(parcel: Parcel): UrlImageSavedState =
            UrlImageSavedState(parcel)

        override fun newArray(size: Int): Array<UrlImageSavedState?> =
            arrayOfNulls(size)
    }
}