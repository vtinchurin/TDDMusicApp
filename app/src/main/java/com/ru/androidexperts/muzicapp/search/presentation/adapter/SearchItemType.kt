package com.ru.androidexperts.muzicapp.search.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ru.androidexperts.muzicapp.core.adapter.GenericViewHolder
import com.ru.androidexperts.muzicapp.core.adapter.RecyclerActions
import com.ru.androidexperts.muzicapp.core.adapter.RecyclerItemType
import com.ru.androidexperts.muzicapp.databinding.ItemErrorBinding
import com.ru.androidexperts.muzicapp.databinding.ItemNoSongsBinding
import com.ru.androidexperts.muzicapp.databinding.ItemProgressBinding
import com.ru.androidexperts.muzicapp.databinding.ItemTrackBinding

interface SearchItemType : RecyclerItemType {

    data object Track : SearchItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions,
        ): GenericViewHolder<SearchItem.Track> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTrackBinding.inflate(inflater, parent, false)
            return SearchViewHolder.Track(binding, clickActions())
        }
    }

    data object Progress : SearchItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions
        ): GenericViewHolder<SearchItem.ProgressUi> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProgressBinding.inflate(inflater, parent, false)
            return SearchViewHolder.Progress(binding)
        }
    }

    data object Error : SearchItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions,
        ): GenericViewHolder<SearchItem.Error> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemErrorBinding.inflate(inflater, parent, false)
            return SearchViewHolder.Error(binding, clickActions())
        }

    }

    data object NoTrack : SearchItemType {
        override fun viewHolder(
            parent: ViewGroup,
            clickActions: RecyclerActions,
        ): GenericViewHolder<SearchItem.NoTracksUi> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemNoSongsBinding.inflate(inflater, parent, false)
            return SearchViewHolder.NoTracks(binding)
        }
    }

}