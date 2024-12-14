package com.ru.androidexperts.muzicapp.search.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView : AppCompatTextView, UpdateText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun update(newText: String) {
        text = newText
    }

    override fun update(textResId: Int) {
        setText(textResId)
    }

    override fun getFreezesText() = true
}