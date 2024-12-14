package com.ru.androidexperts.muzicapp.search.presentation.adapter

import com.ru.androidexperts.muzicapp.core.adapter.GenericViewHolder
import com.ru.androidexperts.muzicapp.databinding.ItemErrorBinding
import com.ru.androidexperts.muzicapp.databinding.ItemNoSongsBinding
import com.ru.androidexperts.muzicapp.databinding.ItemProgressBinding
import com.ru.androidexperts.muzicapp.databinding.ItemTrackBinding

interface SearchViewHolder {

    data class Track(
        private val binding: ItemTrackBinding,
        private val clickActions: SearchScreenActions.TogglePlayPause,
    ) : GenericViewHolder<SearchItem.Track>(binding.root) {
        override fun bind(recyclerItem: SearchItem.Track) {
            binding.playButton.setOnClickListener {
                recyclerItem.playOrStop(clickActions)
            }
            recyclerItem.show(
                binding.imageView,
                binding.authorName,
                binding.songName,
                binding.playButton,
            )
        }
    }

    data class Progress(
        private val binding: ItemProgressBinding,
    ) : GenericViewHolder<SearchItem.ProgressUi>(binding.root)

    data class Error(
        private val binding: ItemErrorBinding,
        private val clickActions: SearchScreenActions.Retry,
    ) : GenericViewHolder<SearchItem.Error>(binding.root) {
        override fun bind(recyclerItem: SearchItem.Error) {
            binding.retryButton.setOnClickListener {
                clickActions.retry()
            }
            recyclerItem.show(
                binding.errorTextView
            )
        }
    }

    data class NoTracks(
        private val binding: ItemNoSongsBinding,
    ) : GenericViewHolder<SearchItem.NoTracksUi>(binding.root)
}