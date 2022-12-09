package com.bloidonia

private val input = """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
""".trimIndent()

fun priority(a: Char) = if (a.isLowerCase()) a - 'a' + 1 else a - 'A' + 27

fun backpack(input: String): Int {
    val (a, b) = input.chunked(input.length / 2)
    val dupe = a.find { b.contains(it) }!!
    return priority(dupe)
}

fun inall(backpacks: List<String>): Int {
    val a = backpacks.map { a -> a.toSet() }.reduce { a, b -> a intersect b }
    return priority(a.first())
}

private fun part1(input: String) = input.split("\n").map(::backpack).sum()

private fun part2(input: String) = input.split("\n").chunked(3).map(::inall).sum()

fun main() {
    println(part1(input))
    println(part2(input))

    val input = object {}::class.java.getResourceAsStream("/day03.input")?.bufferedReader()!!.readText()
    println(part1(input))
    println(part2(input))
}