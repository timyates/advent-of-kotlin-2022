package com.bloidonia

private val input = """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
""".trimIndent()

private typealias Line = Pair<Pair<Int, Int>, Pair<Int, Int>>

private fun parse(input: String): Line {
    val (a, b) = input.split(",")
    val (a1, a2) = a.split("-")
    val (b1, b2) = b.split("-")
    return Pair(Pair(a1.toInt(), a2.toInt()), Pair(b1.toInt(), b2.toInt()))
}

private fun contains(line: Line) =
    line.first.first <= line.second.first && line.first.second >= line.second.second ||
        line.second.first <= line.first.first && line.second.second >= line.first.second

private fun overlaps(line: Line) =
    line.first.first <= line.second.first && line.first.second >= line.second.first ||
            line.second.first <= line.first.first && line.second.second >= line.first.first

private fun count(input: String, fn: (Line) -> Boolean) = input.split("\n").map(::parse)
    .map(fn)
    .count { it }

fun main() {
    println(count(input, ::contains))
    println(count(input, ::overlaps))

    val input = object {}::class.java.getResourceAsStream("/day04.input")?.bufferedReader()!!.readText()
    println(count(input, ::contains))
    println(count(input, ::overlaps))

}