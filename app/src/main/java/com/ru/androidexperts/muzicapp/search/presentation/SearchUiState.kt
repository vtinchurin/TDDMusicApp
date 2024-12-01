package com.ru.androidexperts.muzicapp.search.presentation

import com.ru.androidexperts.muzicapp.search.presentation.adapter.GenericAdapter
import com.ru.androidexperts.muzicapp.search.presentation.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.di.UiState
import com.ru.androidexperts.muzicapp.search.presentation.view.UpdateText

interface SearchUiState : UiState {

    fun show(
        input: UpdateText,
        adapter: GenericAdapter,
    )

    fun recyclerState(): List<RecyclerItem>

    abstract class Single(
        private val recyclerItem: RecyclerItem,
    ) : SearchUiState {

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            adapter.update(listOf(recyclerItem))
        }

        override fun recyclerState() = listOf(recyclerItem)
    }

    abstract class Multiple(
        private val recyclerState: List<RecyclerItem>,
    ) : SearchUiState {

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            adapter.update(recyclerState)
        }

        override fun recyclerState() = recyclerState
    }

    data class Initial(
        private val inputText: String = "",
        private val recyclerState: List<RecyclerItem> = listOf(),
    ) : Multiple(recyclerState) {

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            if (inputText != "") input.update(inputText)
            super.show(input, adapter)
        }

        override fun recyclerState() = recyclerState
    }

    data class Success(
        private val recyclerState: List<RecyclerItem>,
    ) : Multiple(recyclerState = recyclerState)

    data class Error(private val errorResId: Int) :
        Single(recyclerItem = RecyclerItem.ErrorUi(resId = errorResId))

    object Loading : Single(recyclerItem = RecyclerItem.ProgressUi)

    object NoTracks : Single(recyclerItem = RecyclerItem.NoTracksUi)
}