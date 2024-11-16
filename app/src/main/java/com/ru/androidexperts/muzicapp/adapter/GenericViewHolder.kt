package com.ru.androidexperts.muzicapp.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ru.androidexperts.muzicapp.databinding.ItemErrorBinding
import com.ru.androidexperts.muzicapp.databinding.ItemNoSongsBinding
import com.ru.androidexperts.muzicapp.databinding.ItemProgressBinding
import com.ru.androidexperts.muzicapp.databinding.ItemTrackBinding
import com.ru.androidexperts.muzicapp.presentation.adapter.RecyclerActions

abstract class GenericViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    open fun bind(recyclerItem: RecyclerItem) = Unit

    data class Track(
        private val binding: ItemTrackBinding,
        private val clickActions: RecyclerActions.TogglePlayPause
    ) : GenericViewHolder(binding.root) {
        override fun bind(recyclerItem: RecyclerItem) {
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
        private val binding: ItemProgressBinding
    ) : GenericViewHolder(binding.root)

    data class Error(
        private val binding: ItemErrorBinding,
        private val clickActions: RecyclerActions.Retry
    ) : GenericViewHolder(binding.root) {
        override fun bind(recyclerItem: RecyclerItem) {
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
    ) : GenericViewHolder(binding.root)
}