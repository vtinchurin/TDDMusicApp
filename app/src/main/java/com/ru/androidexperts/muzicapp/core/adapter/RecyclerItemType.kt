package com.ru.androidexperts.muzicapp.core.adapter

import android.view.ViewGroup

interface RecyclerItemType {
    fun viewHolder(
        parent: ViewGroup,
        clickActions: RecyclerActions,
    ): GenericViewHolder<RecyclerItem>
}