package com.ru.androidexperts.muzicapp.matchers

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class DrawableMatcher(private val drawableId: Int) : TypeSafeMatcher<View>() {

    private var expectedResourceName: String = ""

    override fun describeTo(description: Description?) {
        description?.appendText("Drawable did not match $expectedResourceName");
    }

    override fun matchesSafely(item: View?): Boolean {

        if (item !is ImageView) {
            return false
        }
        if (drawableId < 0) {
            return item.drawable == null
        }
        val resources: Resources = item.getContext().resources
        expectedResourceName = resources.getResourceEntryName(drawableId)
        val expectedDrawable = resources.getDrawable(drawableId, null)
        val bitmap = item.drawable.toBitmap()
        return bitmap.sameAs(expectedDrawable.toBitmap())
    }
}

