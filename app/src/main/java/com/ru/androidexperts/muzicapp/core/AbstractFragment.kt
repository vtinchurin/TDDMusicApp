package com.ru.androidexperts.muzicapp.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ru.androidexperts.muzicapp.di.core.viewmodels.ViewModelTag
import com.ru.androidexperts.muzicapp.uiObservable.UiObserver

interface AbstractFragment {

    abstract class Ui<binding : ViewBinding> : Fragment(), AbstractFragment {
        private var _binding: binding? = null
        protected val binding get() = _binding!!
        protected abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): binding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View {
            _binding = inflate(inflater, container)
            return binding.root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

    abstract class Async<binding : ViewBinding, uiState : UiState, viewModel : ViewModelTag.AbstractAsync<uiState>> :
        Ui<binding>() {

        protected abstract val update: UiObserver<uiState>
        protected lateinit var viewModel: viewModel

        override fun onResume() {
            super.onResume()
            viewModel.startUpdates(update)
        }

        override fun onPause() {
            super.onPause()
            viewModel.stopUpdates()
        }
    }
}