package com.bloidonia

private val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

private val shapes = listOf(
    listOf(0L to 0L, 1L to 0L, 2L to 0L, 3L to 0L),
    listOf(1L to 0L, 0L to 1L, 1L to 1L, 2L to 1L, 1L to 2L),
    listOf(0L to 0L, 1L to 0L, 2L to 0L, 2L to 1L, 2L to 2L),
    listOf(0L to 0L, 0L to 1L, 0L to 2L, 0L to 3L),
    listOf(0L to 0L, 1L to 0L, 0L to 1L, 1L to 1L)
)

private fun parseGusts(input: String) = generateSequence { input.asSequence() }.flatten().map {
    when (it) {
        '<' -> -1
        '>' -> 1
        else -> throw IllegalArgumentException("Unknown character: $it")
    }
}

private fun part1(input: String, drops: Long = 2022): Long {
    val gusts = parseGusts(input).iterator()
    val shapes = generateSequence { shapes }.flatten().iterator()
    var currentShape: List<Pair<Long, Long>>? = null
    var newDrop = true
    var stopped = 0L
    var gust = true
    val cave = mutableSetOf<Pair<Long, Long>>()
    var top = 0L
    while (stopped < drops) {
        if (newDrop) {
//            println("New drop at ${cave.maxOfOrNull { it.second + 1 } ?: 0}")
            currentShape = shapes.next().map { (it.first + 2) to (it.second + top + 3) }
            newDrop = false
            gust = true
        } else if (gust) {
            gust = false
            val nextGust = gusts.next()
            currentShape!!.map { it.copy(it.first + nextGust) }.let { newShape ->
                if (!newShape.any { cave.contains(it) || it.first < 0 || it.first >= 7 }) {
//                    println("Rock gusted ${if (nextGust < 0) "left" else "right"}")
                    currentShape = newShape
                } else {
//                    println("Rock gusted ${if (nextGust < 0) "left" else "right"} but no effect")
                }
            }
        } else {
            currentShape!!.map { it.copy(second = it.second - 1) }.let { newShape ->
//                println("Rock fell")
                if (newShape.any { cave.contains(it) || it.second < 0 }) {
                    cave.addAll(currentShape!!)
//                    println("rock stopped ${currentShape}")
                    stopped++
                    newDrop = true
                    if (stopped % 100000 == 0L) {
                        println("Stopped $stopped :: $top :: $cave")
                    }
                }
                currentShape = newShape
            }
            gust = true
        }
        top = cave.maxOfOrNull { it.second + 1 } ?: 0L
        cave.removeAll { it.second < top - 20L }
    }
    return cave.maxOfOrNull { it.second + 1 } ?: -1
}

fun debug(cave: MutableSet<Pair<Int, Int>>, currentShape: List<Pair<Int, Int>>?) {
    val maxY = currentShape?.maxOf { it.second }!!
    (maxY downTo 0).forEach { y ->
        print("|")
        (0 until 7).forEach { x ->
            print(if (cave.contains(x to y)) '#' else if (currentShape.contains(x to y)) 'O' else '.')
        }
        println("|")
    }
    println("+-------+")
    println()
}

fun main() {
    println(part1(input))
    println(part1(input, 1000000000000))
    val input = object {}::class.java.getResourceAsStream("/day17.input")?.bufferedReader()!!.readText()
    println(part1(input))
}