package com.ru.androidexperts.muzicapp.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ru.androidexperts.muzicapp.databinding.FragmentSearchSongsBinding
import com.ru.androidexperts.muzicapp.presentation.adapter.RecyclerActions

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchSongsBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: GenericAdapter

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable?) {
            viewModel.fetch(term = s.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = requireActivity().application.searchViewModel

        adapter = GenericAdapter(
            recyclerActions = object : RecyclerActions.Mutable {
                override fun retry() {
                    viewModel.fetch(term = binding.inputView.text())
                }

                override fun togglePlayPause(trackId: Long) {
                    viewModel.togglePlayPause(trackId = trackId)
                }
            }
        )
        binding.recyclerView.adapter = adapter
        binding.inputView.addTextChangedListener(searchTextWatcher)

        val cachedTerm = viewModel.init(firstRun = savedInstanceState == null)
        binding.inputView.update(cachedTerm)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startUpdates(observer = { listTrackUi ->
            adapter.update(listTrackUi)
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopUpdates()
        binding.inputView.removeTextChangedListener(searchTextWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}