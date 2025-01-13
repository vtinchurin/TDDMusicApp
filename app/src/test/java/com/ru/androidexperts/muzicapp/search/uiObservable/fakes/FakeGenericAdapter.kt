package com.ru.androidexperts.muzicapp.search.uiObservable.fakes

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.adapter.GenericAdapter
import com.ru.androidexperts.muzicapp.core.adapter.RecyclerItem

interface FakeGenericAdapter : GenericAdapter {

    fun assertUpdateCalled(expectedList: List<RecyclerItem>)
    fun assertUpdateCalled(expectedItem: RecyclerItem) = assertUpdateCalled(listOf(expectedItem))


    class Base(private val order: Order) : FakeGenericAdapter {

        override fun assertUpdateCalled(expectedList: List<RecyclerItem>) {
            order.assert(UPDATE_RECYCLER, expectedList)
        }

        override fun update(newList: List<RecyclerItem>) {
            order.add(UPDATE_RECYCLER, newList)
        }
    }

    companion object {
        private const val UPDATE_RECYCLER = "RecyclerAdapter#update"
    }
}