package com.bloidonia

private val input = """
    A Y
    B X
    C Z
""".trimIndent()

private val scores = mapOf(
    "X" to 1,
    "Y" to 2,
    "Z" to 3
)
private val equality = mapOf(
    "A" to "X",
    "B" to "Y",
    "C" to "Z"
)
private val beats = mapOf(
    "X" to "C",
    "Y" to "A",
    "Z" to "B"
)
private val beat = mapOf(
    "C" to "X",
    "A" to "Y",
    "B" to "Z"
)
private val lose = mapOf(
    "A" to "Z",
    "B" to "X",
    "C" to "Y"
)
private val draw = mapOf(
    "A" to "X",
    "B" to "Y",
    "C" to "Z"
)

fun score(a: String, b: String) = scores[b]!! + if (equality[a] == b) 3 else if (beats[b] == a) 6 else 0

fun score(input: String): Int {
    val (a, b) = input.split(Regex("\\s"), 2)
    return score(a, b)
}

fun part2(a: String, b: String): Int {
    val move = if (b == "X") lose[a]!! else if (b == "Y") draw[a]!! else beat[a]!!
    return score(a, move)
}

private fun part2(input: String): Int {
    val (a, b) = input.split(Regex("\\s"), 2)
    return part2(a, b)
}

fun scan(input: String, fn: (String) -> Int) =
    input.split("\n").map(fn).sum()

fun main() {
    println(scan(input, ::score))
    println(scan(input, ::part2))

    val input = object {}::class.java.getResourceAsStream("/day02.input")?.bufferedReader()!!.readText()
    println(scan(input, ::score))
    println(scan(input, ::part2))
}