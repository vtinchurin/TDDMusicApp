package com.ru.androidexperts.muzicapp.search.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.androidexperts.muzicapp.core.AbstractFragment
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.databinding.FragmentSearchSongsBinding
import com.ru.androidexperts.muzicapp.di.ProvideViewModel
import com.ru.androidexperts.muzicapp.search.presentation.adapter.LastItemDecorator
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchAdapter

class SearchFragment :
    AbstractFragment.Async<FragmentSearchSongsBinding, SearchUiState, SearchViewModel>() {

    override val update = UiObserver<SearchUiState> {
        it.show(
            input = binding.inputView,
            adapter = searchAdapter
        )
    }
    private lateinit var searchAdapter: SearchAdapter
    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable?) {
            viewModel.fetch(term = s.toString())
        }
    }

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchSongsBinding = FragmentSearchSongsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            (requireActivity() as ProvideViewModel).makeViewModel(SearchViewModel::class.java)

        searchAdapter = SearchAdapter(clickActions = viewModel)
        with(binding.recyclerView) {
            adapter = searchAdapter
            addItemDecoration(LastItemDecorator())
        }
        viewModel.init(isFirstRun = savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        binding.inputView.addTextChangedListener(searchTextWatcher)
    }

    override fun onPause() {
        super.onPause()
        binding.inputView.removeTextChangedListener(searchTextWatcher)
    }
}