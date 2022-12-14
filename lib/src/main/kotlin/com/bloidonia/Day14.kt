package com.bloidonia

import kotlin.math.sign

private val input = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
""".trimIndent()

private val cave = mutableSetOf<Pair<Int, Int>>()
private fun line(pairs: List<Pair<Int, Int>>) = pairs.take(2).let { (a, b) ->
    var curr = a
    val d = (b.first - a.first).sign to (b.second - a.second).sign
    cave.add(b)
    while (curr != b) {
        cave.add(curr)
        curr = curr.copy(curr.first + d.first, curr.second + d.second)
    }
}

private fun setLocations(input: String) = input.split(Regex("->"))
    .map { it.split(",").let { it[0].trim().toInt() to it[1].trim().toInt() } }
    .windowed(2, 1)
    .forEach(::line)

private fun process(input: String, part2: Boolean) {
    cave.clear()
    input.split("\n").forEach(::setLocations)
    val walls = cave.size
    val theVoid = if (!part2) cave.maxOf { it.second } else cave.maxOf { it.second } + 2
    var falling = Pair(500, 0)

    while (part2 || falling.second < theVoid) {
        val hasBlockBelow =
            cave.contains(falling.first to falling.second + 1) || (part2 && (falling.second >= theVoid - 1))
        val hasBlockLeft =
            cave.contains(falling.first - 1 to falling.second + 1) || (part2 && (falling.second >= theVoid - 1))
        val hasBlockRight =
            cave.contains(falling.first + 1 to falling.second + 1) || (part2 && (falling.second >= theVoid - 1))

        falling = if (!hasBlockBelow) {
            falling.copy(second = falling.second + 1)
        } else if (!hasBlockLeft) {
            falling.copy(falling.first - 1, falling.second + 1)
        } else if (!hasBlockRight) {
            falling.copy(falling.first + 1, falling.second + 1)
        } else {
            // Can't move, so it stays here
            cave.add(falling)
            // If we're doing part2, and it's stopped at the top then we're done
            if (part2 && falling == Pair(500, 0)) {
                break
            }
            // New drip
            Pair(500, 0)
        }
    }
    println(cave.size - walls)
}

fun main() {
    process(input, false)
    process(input, true)
    val input = object {}::class.java.getResourceAsStream("/day14.input")?.bufferedReader()!!.readText()
    process(input, false)
    process(input, true)
}
