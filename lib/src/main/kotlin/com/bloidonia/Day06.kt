package com.bloidonia

private val input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
private val input2 = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"

private fun findIndex(input: String, len: Int = 4) =
    input.asSequence()
        .windowed(len, 1)
        .withIndex()
        .first { it.value.distinct().size == len }
        .index + len

fun main() {
    println(findIndex(input))
    println(findIndex(input2))

    println(findIndex(input, 14))
    println(findIndex(input2, 14))

    val input = object {}::class.java.getResourceAsStream("/day06.input")?.bufferedReader()!!.readText()
    println(findIndex(input))
    println(findIndex(input, 14))
}