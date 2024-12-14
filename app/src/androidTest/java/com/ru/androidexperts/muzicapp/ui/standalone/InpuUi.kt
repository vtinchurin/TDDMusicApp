package com.ru.androidexperts.muzicapp.ui.standalone

import android.view.View
import android.widget.FrameLayout
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.Ui
import org.hamcrest.Matcher

class InputUi(matchers: List<Matcher<View>>) : Ui.Container(
    id = R.id.inputView,
    classType = FrameLayout::class.java,
    matchers = matchers
) {

    private val editText: EditTextUi = EditTextUi.Base(
        id = R.id.inputEditText,
        matchers = descendantMatchers()
    )

    override fun assertVisible() {
        super.assertVisible()
        editText.assertVisible()
    }

    fun addInput(text: String) {
        editText.addInput(text)
    }
}