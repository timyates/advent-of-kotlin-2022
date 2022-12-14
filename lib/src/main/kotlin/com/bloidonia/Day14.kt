package com.bloidonia


private val input = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
""".trimIndent()

private val cave = mutableSetOf<Pair<Int, Int>>()
private fun line(pairs: List<Pair<Int, Int>>) {
    val a = pairs[0]
    val b = pairs[1]
    var curr = a
    val dx = if (a.first < b.first) 1 else if (a.first == b.first) 0 else -1
    val dy = if (a.second < b.second) 1 else if (a.second == b.second) 0 else -1
    cave.add(b)
    while (curr != b) {
        cave.add(curr)
        curr = curr.copy(curr.first + dx, curr.second + dy)
    }
}

private fun setLocations(input: String) {
    input.split(Regex("->"))
        .map { it.split(",").let { it[0].trim().toInt() to it[1].trim().toInt() } }
        .windowed(2, 1)
        .forEach(::line)
}

private fun process(input: String, part1: Boolean) {
    cave.clear()
    input.split("\n").forEach(::setLocations)
    val walls = cave.size
    val theVoid = if (part1) cave.maxOf { it.second } else cave.maxOf { it.second } + 2
    var falling = Pair(500, 0)
    while (!part1 || falling.second < theVoid) {
        val hasBelow = cave.contains(falling.first to falling.second + 1) || (!part1 && (falling.second >= theVoid - 1))
        val hasLeft = cave.contains(falling.first - 1 to falling.second + 1) || (!part1 && (falling.second >= theVoid - 1))
        val hasRight = cave.contains(falling.first + 1 to falling.second + 1) || (!part1 && (falling.second >= theVoid - 1))
        falling = if (!hasBelow) {
            falling.copy(falling.first, falling.second + 1)
        } else if (!hasLeft) {
            falling.copy(falling.first - 1, falling.second + 1)
        } else if (!hasRight) {
            falling.copy(falling.first + 1, falling.second + 1)
        } else {
            cave.add(falling)
            if (!part1 && falling == Pair(500, 0)) {
                break
            }
            Pair(500, 0)
        }
    }
    println(cave.size - walls)
}

fun main() {
    process(input, true)
    process(input, false)
    val input = object {}::class.java.getResourceAsStream("/day14.input")?.bufferedReader()!!.readText()
    process(input, true)
    process(input, false)
}
