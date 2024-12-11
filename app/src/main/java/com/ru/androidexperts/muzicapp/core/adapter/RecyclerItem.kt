package com.ru.androidexperts.muzicapp.core.adapter

interface RecyclerItem {

    fun type(): RecyclerItemType

    fun id(): String = this.javaClass.simpleName

}