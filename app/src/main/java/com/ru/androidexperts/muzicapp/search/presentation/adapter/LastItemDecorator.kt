package com.ru.androidexperts.muzicapp.search.presentation.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemDecorator(
    private val padding: Int = 250,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
            .let {
                if (it == RecyclerView.NO_POSITION) return
                else it
            }
        parent.adapter?.itemCount?.let {
            if (it > 1 && position == it - 1)
                outRect.bottom = padding
        }
    }
}