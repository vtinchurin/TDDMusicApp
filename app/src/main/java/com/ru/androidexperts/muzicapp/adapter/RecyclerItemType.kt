package com.ru.androidexperts.muzicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ru.androidexperts.muzicapp.databinding.ItemErrorBinding
import com.ru.androidexperts.muzicapp.databinding.ItemNoSongsBinding
import com.ru.androidexperts.muzicapp.databinding.ItemProgressBinding
import com.ru.androidexperts.muzicapp.databinding.ItemTrackBinding
import com.ru.androidexperts.muzicapp.presentation.adapter.RecyclerActions

interface RecyclerItemType {

    fun viewHolder(parent: ViewGroup, clickActions: RecyclerActions.Mutable): GenericViewHolder

    data object Track : RecyclerItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions.Mutable
        ): GenericViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTrackBinding.inflate(inflater, parent, false)
            return GenericViewHolder.Track(binding, clickActions)
        }
    }

    data object Progress : RecyclerItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions.Mutable
        ): GenericViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProgressBinding.inflate(inflater, parent, false)
            return GenericViewHolder.Progress(binding)
        }
    }

    data object Error : RecyclerItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions.Mutable
        ): GenericViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemErrorBinding.inflate(inflater, parent, false)
            return GenericViewHolder.Error(binding, clickActions)
        }

    }

    data object NoTrack : RecyclerItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions.Mutable
        ): GenericViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemNoSongsBinding.inflate(inflater, parent, false)
            return GenericViewHolder.NoTracks(binding)
        }
    }
}