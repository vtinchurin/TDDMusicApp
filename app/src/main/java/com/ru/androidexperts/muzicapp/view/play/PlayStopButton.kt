package com.ru.androidexperts.muzicapp.view.play

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes

class PlayStopButton : View, UpdateBackgroundRes {

    private var backgroundResId = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedState = ResIdSavedState(it)
            savedState.save(backgroundResId)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as ResIdSavedState
        super.onRestoreInstanceState(restoredState.superState)
        update(restoredState.restore())
    }

    override fun update(newRes: Int) {
        backgroundResId = newRes
        setBackgroundResource(newRes)
    }
}

interface UpdateBackgroundRes {

    fun update(@DrawableRes newRes: Int)
}