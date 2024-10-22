package com.ru.androidexperts.muzicapp.view.play

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes

class PlayStopButton : View, UpdatePlayStopButton {

    private lateinit var state: PlayStopUiState

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedState = PlayStopButtonSavedState(it)
            savedState.save(state)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as PlayStopButtonSavedState
        super.onRestoreInstanceState(restoredState.superState)
        update(restoredState.restore())
    }

    override fun update(uiState: PlayStopUiState) {
        state = uiState
        state.update(this)
    }

    override fun update(newRes: Int) {
        setBackgroundResource(newRes)
    }
}

interface UpdatePlayStopButton {

    fun update(uiState: PlayStopUiState)

    fun update(@DrawableRes newRes: Int)
}