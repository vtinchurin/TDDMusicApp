package com.ru.androidexperts.muzicapp.matchers

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class TextColorMatcher(private val colorId: Int) : TypeSafeMatcher<View>() {

    private var expectedColorId = -1

    override fun describeTo(description: Description?) {
        description?.appendText("Color did not match $colorId was $expectedColorId")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextView)
            return false
        if (colorId < 0)
            return false
        expectedColorId = ContextCompat.getColor(item.context, colorId)
        return item.currentTextColor == expectedColorId
    }
}
