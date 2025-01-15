package com.ru.androidexperts.muzicapp.core

import org.junit.Assert.assertEquals

typealias Trace = MutableList<Pair<String, List<Any>>>

interface Order {

    fun add(name: String, args: List<Any> = emptyList())

    fun add(name: String, args: Any) = add(name, listOf(args))

    fun assert(name: String, args: List<Any> = emptyList())

    fun assert(name: String, args: Any) = assert(name, listOf(args))

    fun assertTraceSize(size: Int)

    /**
     * The method [printTrace] is used for debugging purposes.
     * @return Trace are printed in the console.
     */

    fun printTrace()

    data class Base(
        private val trace: Trace = mutableListOf(),
    ) : Order {

        private var index = 0

        override fun add(name: String, args: List<Any>) {
            trace.add(name to args)
        }

        override fun assert(name: String, args: List<Any>) {
            val (n, a) = trace[index]
            assertEquals(n, name)
            assertEquals(a, args)
            assertEquals(name to args, trace[index])
            index++
        }

        override fun assertTraceSize(size: Int) {
            assertEquals(size, trace.size)
        }

        override fun printTrace() {
            println(trace.joinToString(separator = "\n", prefix = "Trace: \n", postfix = "\n"))
        }
    }
}