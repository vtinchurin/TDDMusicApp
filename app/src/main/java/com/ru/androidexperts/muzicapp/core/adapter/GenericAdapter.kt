package com.ru.androidexperts.muzicapp.core.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


interface GenericAdapter {

    fun update(newList: List<RecyclerItem>)

    abstract class Abstract<actions : RecyclerActions>(
        private val clickActions: actions,
        private val typeList: List<RecyclerItemType>,
    ) : RecyclerView.Adapter<GenericViewHolder<RecyclerItem>>(), GenericAdapter {

        private val data = mutableListOf<RecyclerItem>()

        override fun update(newList: List<RecyclerItem>) {
            val callback = DiffUtilCallback(newList)
            val diff = DiffUtil.calculateDiff(callback)
            data.clear()
            data.addAll(newList)
            diff.dispatchUpdatesTo(this)
        }

        override fun getItemViewType(position: Int): Int {
            val type = data[position].type()
            return typeList.indexOf(type)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): GenericViewHolder<RecyclerItem> {
            return typeList[viewType].viewHolder(parent, clickActions)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: GenericViewHolder<RecyclerItem>, position: Int) =
            holder.bind(data[position])

        override fun onBindViewHolder(
            holder: GenericViewHolder<RecyclerItem>,
            position: Int,
            payloads: MutableList<Any>,
        ) {
            holder.bind(data[position])
        }

        private inner class DiffUtilCallback(
            private val newList: List<RecyclerItem>,
        ) : DiffUtil.Callback() {

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
                return true
            }

            override fun getOldListSize(): Int {
                return data.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].id() == newList[newItemPosition].id()
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newList[newItemPosition]
            }

        }
    }
}