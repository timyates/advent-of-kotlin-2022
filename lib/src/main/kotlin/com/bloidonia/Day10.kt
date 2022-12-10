package com.bloidonia

import kotlin.math.abs

private val small = """
    noop
    addx 3
    addx -5
""".trimIndent()

private val input = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
""".trimIndent()

private fun noop() = sequence { yield(0) }
private fun addx(x: Int) = sequence {
    yield(0)
    yield(x)
}

private fun parse(line: String): Sequence<Int> {
    return line.split(" ").let { parts ->
        when (parts[0]) {
            "noop" -> noop()
            "addx" -> addx(parts[1].toInt())
            else -> throw IllegalArgumentException("Unknown op: $parts[0]")
        }
    }
}

private fun seq(input: String) = input.lineSequence().flatMap(::parse)

private fun part1(input: String, vararg indices: Int): Int {
    val numbers = seq(input)
    return indices.map {
        val x = numbers.take(it - 1).sum() + 1
        val ret = it * x
        println("$it: $x: $ret")
        ret
    }.sum()
}

private fun crt(input: String) {
    (0 until (40 * 6)).asSequence().zip(seq(input).runningReduce { acc, v -> acc + v }).map { (pixel, offset) ->
        if (abs(pixel % 40 - offset) <= 1) '#' else '.'
    }.joinToString("").chunked(40).forEach(::println)
}

fun main() {
    println(part1(small, 1, 2, 3, 4, 5))
    println(part1(input, 20, 60, 100, 140, 180, 220))

    crt(input)

    val input = object {}::class.java.getResourceAsStream("/day10.input")?.bufferedReader()!!.readText()
    println("\nPART1\n")
    println(part1(input, 20, 60, 100, 140, 180, 220))
    println("\nPART2\n")
    crt(input)
}