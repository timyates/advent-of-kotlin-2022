package com.bloidonia

import kotlin.math.abs

private val input = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
""".trimIndent()

data class Point(val x: Int, val y: Int)

// Extension method on a List
fun <T> List<T>.replace(index: Int, replacement: T): List<T> = this.subList(0, index) + replacement + this.subList(index + 1, this.size)

data class Rope(val rope: List<Point>, val tailPoints: Set<Point> = setOf(Point(0, 0))) {
    fun moveHead(dx: Int, dy: Int) =
        Rope(rope.replace(0, Point(rope[0].x + dx, y = rope[0].y + dy)), tailPoints).moveTail()

    private fun tailShouldMove(tailIndex: Int) =
        abs(rope[tailIndex - 1].x - rope[tailIndex].x) > 1 || abs(rope[tailIndex - 1].y - rope[tailIndex].y) > 1

    private fun moveTail() = (1 until rope.size).fold(this) { r, t ->
        if (r.tailShouldMove(t)) {
            val (dx, dy) = direction(r, t, Point::x) to direction(r, t, Point::y)
            val newTail = r.rope[t].copy(x = r.rope[t].x + dx, y = r.rope[t].y + dy)
            Rope(r.rope.replace(t, newTail), if (t == rope.size - 1) r.tailPoints + newTail else r.tailPoints)
        } else {
            r
        }
    }

    private fun direction(rope: Rope, index: Int, extract: (Point) -> Int) =
        if (extract(rope.rope[index - 1]) > extract(rope.rope[index]))
            1
        else if (extract(rope.rope[index - 1]) < extract(rope.rope[index]))
            -1
        else 0
}

private fun part1(input: String, knots: Int = 2) {

    val rope = input.lines().fold(Rope(List(knots) { Point(0, 0) })) { rope, line ->
        val (dir, dist) = line.split(" ")
        val (dx, dy) = when (dir) {
            "U" -> 0 to 1
            "D" -> 0 to -1
            "L" -> -1 to 0
            "R" -> 1 to 0
            else -> throw IllegalArgumentException("Unknown direction: $dir")
        }
        (1..dist.toInt()).fold(rope) { rope, _ -> rope.moveHead(dx, dy) }
    }
    println(rope.tailPoints.size)
}

fun main() {
    part1(input)
    part1(input, 10)
    val input = object {}::class.java.getResourceAsStream("/day09.input")?.bufferedReader()!!.readText()
    part1(input)
    part1(input, 10)
}