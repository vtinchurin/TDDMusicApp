package com.ru.androidexperts.muzicapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ru.androidexperts.muzicapp.presentation.adapter.RecyclerActions


interface GenericAdapter {
    fun update(newList: List<RecyclerItem>)

    class Base(
        private val clickActions: RecyclerActions.Mutable,
        private val typeList: List<RecyclerItemType> = listOf(
            RecyclerItemType.Track,
            RecyclerItemType.Progress,
            RecyclerItemType.Error,
            RecyclerItemType.NoTrack,
        )
    ) : RecyclerView.Adapter<GenericViewHolder>(), GenericAdapter {

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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
            return typeList[viewType].viewHolder(parent, clickActions)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: GenericViewHolder, position: Int) =
            holder.bind(data[position])


        private inner class DiffUtilCallback(
            private val newList: List<RecyclerItem>,
        ) : DiffUtil.Callback() {

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