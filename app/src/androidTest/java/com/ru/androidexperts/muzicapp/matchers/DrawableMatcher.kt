package com.ru.androidexperts.muzicapp.matchers

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageButton
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class DrawableMatcher(private val drawableId: Int) : TypeSafeMatcher<View>() {

    private var resourceName: String = ""

    override fun describeTo(description: Description?) {
        description?.appendText("Color did not match $drawableId was $resourceName");
    }

    override fun matchesSafely(item: View?): Boolean {

        if (item !is ImageButton) {
            return false
        }
        if (drawableId < 0) {
            return item.drawable == null
        }
        val resources: Resources = item.getContext().resources
        val expectedDrawable = resources.getDrawable(drawableId)
        resourceName = resources.getResourceEntryName(drawableId)

        if (expectedDrawable == null) {
            return false
        }

        val bitmap = (item.drawable as BitmapDrawable).bitmap
        val otherBitmap = (expectedDrawable as BitmapDrawable).bitmap
        return bitmap.sameAs(otherBitmap)
    }
}

