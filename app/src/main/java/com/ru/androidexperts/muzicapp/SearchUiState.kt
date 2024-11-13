package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.adapter.GenericAdapter
import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.view.UpdateText

interface SearchUiState {

    fun show(
        input: UpdateText,
        adapter: GenericAdapter,
    )

    fun recyclerState(): List<RecyclerItem>

    abstract class Abstract(
        private val recyclerState: List<RecyclerItem>,
    ) : SearchUiState {

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            adapter.update(recyclerState)
        }

        override fun recyclerState() = recyclerState
    }

    data class Initial(private val inputText: String = "") : Abstract(recyclerState = listOf()) {

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            if (inputText != "") input.update(inputText)
            else adapter.update(listOf())
        }
    }

    data class Success(
        private val recyclerState: List<RecyclerItem>,
    ) : Abstract(recyclerState = recyclerState)

    data class Error(private val errorItem: RecyclerItem) :
        Abstract(recyclerState = listOf(errorItem))

    object Loading : Abstract(recyclerState = listOf(RecyclerItem.ProgressUi))

    object NoTracks : Abstract(recyclerState = listOf(RecyclerItem.NoTracksUi))
}