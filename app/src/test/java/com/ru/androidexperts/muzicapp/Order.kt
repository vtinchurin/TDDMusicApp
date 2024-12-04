package com.ru.androidexperts.muzicapp

import org.junit.Assert.assertEquals

class Order {
    private val trace = mutableListOf<String>()
    fun add(action: String){
        trace.add(action)
    }
    fun check(expectedTrace: List<String>){
        assertEquals(expectedTrace, trace)
    }
}