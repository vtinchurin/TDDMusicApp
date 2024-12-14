package com.ru.androidexperts.muzicapp.core.matchers

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class DrawableMatcher(private val drawableId: Int) : TypeSafeMatcher<View>() {

    private var expectedResourceName: String = ""

    override fun describeTo(description: Description?) {
        description?.appendText("Drawable did not match $expectedResourceName")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) {
            return false
        }
        if (drawableId < 0) {
            return item.background == null
        }

        val resources: Resources = item.context.resources
        expectedResourceName = resources.getResourceEntryName(drawableId)
        val expectedDrawable = resources.getDrawable(drawableId, item.context.theme)
        val bitmap = item.background.toBitmap()
        return bitmap.sameAs(expectedDrawable.toBitmap())
    }
}
