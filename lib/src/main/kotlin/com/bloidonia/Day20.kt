package com.bloidonia

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

private val input = """
    1
    2
    -3
    3
    -2
    0
    4
""".trimIndent()

private data class Elem(val value: Long, var index: Long)
private fun parse(input: String, mult: Long = 1L) = input.lines().withIndex().map { Elem(it.value.toLong() * mult, it.index.toLong()) }

private fun mod(x: Long, n: Int): Long = Math.floorMod(x, n.toLong())

private fun churn(input: String, mult: Long = 1L, rounds: Int = 1) {
    val inorder = parse(input, mult)
    for (round in 1..rounds) {
        print(".")
        inorder.forEach { e ->
            val currentIndex = e.index
            val newIndex = mod(e.index + e.value, inorder.size - 1).let { if (it == 0L) inorder.size - 1L else it }
            if (e.value == 0L) {
                return@forEach
            }
            if (newIndex > currentIndex) {
                inorder.filter { it.index > min(currentIndex, newIndex) && it.index <= max(currentIndex, newIndex) }
                    .forEach {
                        it.index -= 1
                    }
            } else {
                inorder.filter { it.index >= min(currentIndex, newIndex) && it.index <= max(currentIndex, newIndex) }
                    .forEach {
                        it.index += 1
                    }
            }
            e.index = newIndex
        }
    }
    val sum = inorder.find { it.value == 0L }!!.index.let { idx ->
        listOf(1000, 2000, 3000).map { add ->
            inorder.find { it.index == ((idx + add) % inorder.size) }!!.value
        }.sum()
    }
    println(" : $sum")
}


fun main() {
    churn(input)
    churn(input, 811589153L, 10)
    val input = object {}::class.java.getResourceAsStream("/day20.input")?.bufferedReader()!!.readText()
    churn(input)
    churn(input, 811589153L, 10)
}
