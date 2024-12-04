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
        private val inputText: String
    ) : SearchUiState {

        override fun show(input: UpdateText, adapter: GenericAdapter) {
            adapter.update(recyclerState)
            if (inputText != "")
                input.update(inputText)
        }

        override fun recyclerState() = recyclerState
    }
    object Initial : Abstract(recyclerState = listOf(), inputText = "")

    data class Success(
        private val recyclerState: List<RecyclerItem>,
        private val inputText: String
    ) : Abstract(recyclerState = recyclerState, inputText = inputText)

    data class Error(private val message: String, private val inputText: String) :
        Abstract(recyclerState = listOf(RecyclerItem.ErrorUi(message)), inputText = input

    data class Load(private val inputText: String) :
        Abstract(recyclerState = listOf(RecyclerItem.ProgressUi), inputText = inputText)

    data class NoTracks(private val inputText: String) :
        Abstract(recyclerState = listOf(RecyclerItem.NoTracksUi), inputText = inputText)
}