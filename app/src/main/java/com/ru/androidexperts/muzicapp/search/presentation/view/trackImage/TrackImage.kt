package com.ru.androidexperts.muzicapp.search.presentation.view.trackImage

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.google.android.material.imageview.ShapeableImageView
import com.ru.androidexperts.muzicapp.di.ProvidePicEngine

class TrackImage : ShapeableImageView, TrackImageUpdate {

    private var state: TrackImageUiState = TrackImageUiState.Base("")
    private val animation = ObjectAnimator.ofFloat(
        this,
        "rotation", 0f, 360f
    ).apply {
        setDuration(6000)
        interpolator = LinearInterpolator()
        repeatCount = Animation.INFINITE
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun update(state: TrackImageUiState) {
        this.state = state
        state.update(this)
    }

    override fun startAnimation() {
        animation.start()
    }

    override fun stopAnimation() {
        animation.setCurrentFraction(0f)
        animation.cancel()
    }

    override fun show(url: String) {
        (context.applicationContext as ProvidePicEngine).picEngine().loadImage(this, url)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedState = TrackImageSavedState(it)
            savedState.save(state)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as TrackImageSavedState
        super.onRestoreInstanceState(restoredState.superState)
        update(restoredState.restore())
    }
}