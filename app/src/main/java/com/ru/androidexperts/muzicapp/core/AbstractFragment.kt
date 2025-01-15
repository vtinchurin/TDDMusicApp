package com.ru.androidexperts.muzicapp.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.di.ViewModelTag

/**
 * B - [ViewBinding]
 * U - [UiState]
 * V - [ViewModelTag]
 */
interface AbstractFragment {

    abstract class Ui<B : ViewBinding> : Fragment(), AbstractFragment {
        private var _binding: B? = null
        protected val binding get() = _binding!!
        protected abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): B

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

    abstract class Async<B : ViewBinding, U : UiState, V : ViewModelTag.AbstractAsync<U>> :
        Ui<B>() {

        protected abstract val update: UiObserver<U>
        protected lateinit var viewModel: V

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