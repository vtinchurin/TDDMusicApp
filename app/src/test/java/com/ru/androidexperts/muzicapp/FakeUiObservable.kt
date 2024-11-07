package com.ru.androidexperts.muzicapp

import org.junit.Assert.assertEquals

interface FakeUiObservable<T> : UiObservable<T> {
    fun assertPostUiStateCalledCount(count: Int)
    fun assertRegisterCalledCount(count: Int)
    fun assertPostUiStates(uiStates: List<T>)
    abstract class Abstract<T : Any>(
        private val order: Order
    ) : FakeUiObservable<T> {
        private var uiStateCached: T? = null
        private var observerCached: ((T) -> Unit)? = null
        private var unregisterCalledCount = 0
        private var registerCalledCount = 0
        private var postUiStatesListCalled = mutableListOf<T>()
        override fun register(observer: (T) -> Unit) {
            order.add(OBSERVABLE_REGISTER)
            registerCalledCount++
            observerCached = observer
            if (uiStateCached != null) {
                observerCached!!.invoke(uiStateCached!!)
            }
        }
        override fun unregister() {
            order.add(OBSERVABLE_UNREGISTER)
            unregisterCalledCount++
            observerCached = null
        }
        override fun postUiState(uiState: T) {
            postUiStatesListCalled.add(uiState)
            order.add(OBSERVABLE_POST)
            if (observerCached == null) {
                uiStateCached = uiState
            } else {
                observerCached!!.invoke(uiState)
            }
        }

        override fun assertPostUiStateCalledCount(count: Int) {
            assertEquals(count, postUiStatesListCalled.size)
        }

        override fun assertRegisterCalledCount(count: Int) {
            assertEquals(count, registerCalledCount)
        }

        override fun assertPostUiStates(uiStates: List<T>) {
            assertEquals(uiStates, postUiStatesListCalled)
        }
    }
}
const val OBSERVABLE_REGISTER = "observable register"
const val OBSERVABLE_UNREGISTER = "observable unregister"
const val OBSERVABLE_POST= "observable post"