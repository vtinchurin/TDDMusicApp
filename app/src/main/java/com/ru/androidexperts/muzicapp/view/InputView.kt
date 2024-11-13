package com.ru.androidexperts.muzicapp.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ru.androidexperts.muzicapp.databinding.InputBinding

class InputView : FrameLayout, UpdateText {

    private val binding = InputBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun update(newText: String) {
        if(newText != text())
            binding.inputEditText.setText(newText)
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        binding.inputEditText.addTextChangedListener(textWatcher)
    }

    fun removeTextChangedListener(textWatcher: TextWatcher) {
        binding.inputEditText.removeTextChangedListener(textWatcher)
    }

    fun text() = binding.inputEditText.text.toString()
}