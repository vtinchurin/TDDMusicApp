package com.ru.androidexperts.muzicapp.view.image

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.ru.androidexperts.muzicapp.R
import com.squareup.picasso.Picasso

class CustomImageView : AppCompatImageView, UpdateImageUrl {

    private var imageUrl = ""

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val savedState = UrlImageSavedState(it)
            savedState.save(imageUrl)
            return savedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as UrlImageSavedState
        super.onRestoreInstanceState(restoredState.superState)
        update(restoredState.restore())
    }

    override fun update(newUrl: String) {
        imageUrl = newUrl
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.ic_artwork)
            .error(R.drawable.ic_artwork)
            .into(this)
    }

    fun imageUrl() = imageUrl
}

interface UpdateImageUrl {

    fun update(newUrl: String)
}