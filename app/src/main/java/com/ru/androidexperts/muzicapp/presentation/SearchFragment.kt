package com.ru.androidexperts.muzicapp.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ru.androidexperts.muzicapp.databinding.FragmentSearchSongsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), ClickListener.SearchFragment {

    private var _binding: FragmentSearchSongsBinding? = null
    private val binding
        get() = _binding!!
    private var searchJob: Job? = null
    private lateinit var viewModel: SearchViewModel

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

        val adapter = GenericAdapter(
            typeList = listOf(
                UiStateType.Track(clickPlay = this),
                UiStateType.Retry(clickRetry = this),
                UiStateType.Error,
                UiStateType.Progress
            )
        )
        binding.recyclerView.adapter = adapter
        binding.inputView.addTextChangedListener(object : TextWatcher {

            var runnable: Runnable? = null
            val handler = Handler(Looper.getMainLooper())

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                searchJob = lifecycleScope.launch {
                    viewModel.fetch(term = s.toString())
                }
                handler.postDelayed(runnable!!, 300)
            }
        })

        viewModel.startUpdates(observer = { searchUiState ->
            searchUiState.show(term = binding.inputView, track = adapter)
        })

        viewModel.init(firstRun = savedInstanceState == null)
    }

    override fun retry() {
        viewModel.fetch(term = binding.inputView.text())
    }

    override fun togglePlayPause(trackId: Long) {
        viewModel.togglePlayPause(trackId = trackId)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopUpdates()
        binding.inputView.removeTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ClickListener {

    interface Retry : ClickListener {
        fun retry()
    }

    interface TogglePlayPause {
        fun togglePlayPause(trackId: Long)
    }

    interface SearchFragment : Retry, TogglePlayPause
}