package com.ru.androidexperts.muzicapp.core.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class GenericViewHolder<out T : RecyclerItem>(view: View) :
    RecyclerView.ViewHolder(view) {

    open fun bind(recyclerItem: @UnsafeVariance T) = Unit

}
