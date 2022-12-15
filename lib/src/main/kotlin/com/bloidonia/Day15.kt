package com.bloidonia

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val input = """
    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    Sensor at x=9, y=16: closest beacon is at x=10, y=16
    Sensor at x=13, y=2: closest beacon is at x=15, y=3
    Sensor at x=12, y=14: closest beacon is at x=10, y=16
    Sensor at x=10, y=20: closest beacon is at x=10, y=16
    Sensor at x=14, y=17: closest beacon is at x=10, y=16
    Sensor at x=8, y=7: closest beacon is at x=2, y=10
    Sensor at x=2, y=0: closest beacon is at x=2, y=10
    Sensor at x=0, y=11: closest beacon is at x=2, y=10
    Sensor at x=20, y=14: closest beacon is at x=25, y=17
    Sensor at x=17, y=20: closest beacon is at x=21, y=22
    Sensor at x=16, y=7: closest beacon is at x=15, y=3
    Sensor at x=14, y=3: closest beacon is at x=15, y=3
    Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".trimIndent()

private data class Sensor(val pos: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
    val range = abs(pos.first - beacon.first) + abs(pos.second - beacon.second)
    fun covers(y: Int) = pos.second - range <= y && y <= pos.second + range
    fun coverage(y: Int, limit: Int? = null): Sequence<Pair<Int, Int>> = sequence {
        val dx = range - abs(y - pos.second)

        val start = if (limit != null) max(0, (pos.first - dx)) else (pos.first - dx)
        val end = if (limit != null) min(limit, (pos.first + dx)) else (pos.first + dx)
        val xs = (start..end).map { it to y }
        yieldAll(xs)
    }
    fun coverageRange(y: Int, limit: Int) = (range - abs(y - pos.second)).let { dx ->
        max(0, (pos.first - dx))..min(limit, (pos.first + dx))
    }
}

private fun readSensor(input: String): Sensor {
    val (x, y, bx, by) = Regex("Sensor at x=([-\\d]+), y=([-\\d]+): closest beacon is at x=([-\\d]+), y=([-\\d]+)").find(
        input
    )!!.destructured
    return Sensor(x.toInt() to y.toInt(), bx.toInt() to by.toInt())
}

private fun part1(input: String, line: Int): Int {
    val sensors = input
        .lineSequence()
        .map(::readSensor)
        .filter { it.covers(line) }
    return sensors
        .flatMap { it.coverage(line) }
        .toSet().minus(sensors.map { it.beacon }.toSet())
        .size
}
private fun merge(ranges: List<IntRange>): List<IntRange> {
    var start = ranges[0].first
    var end = ranges[0].last
    val result = mutableListOf<IntRange>()
    for (i in 1 until ranges.size) {
        val curr = ranges[i]
        if (curr.first <= end) {
            end = max(curr.last, end)
        } else {
            result.add(start..end)
            start = curr.first
            end = curr.last
        }
    }
    result.add(start..end)
    return result
}

private fun part2(input: String, max: Int): Pair<Int, Int> {
    val sensors = input
        .lineSequence()
        .map(::readSensor)
        .toList()
    for (y in 0..max) {
        val a = sensors.filter { it.covers(y) }.map { it.coverageRange(y, max) }
        val sorted = merge(a.sortedBy { it.first })
        if (sorted.size > 1) {
            return sorted[0].last + 1 to y
        }
        if (sorted[0].first != 0) {
            return 0 to y
        }
        if (sorted[0].last != max) {
            return max to y
        }
    }
    return -1 to -1
}

fun main() {
    println(part1(input, 10))
    part2(input, 20).let {
        println("$it => ${it.first * 4000000L + it.second}")
    }
    val now = System.currentTimeMillis()
    val input = object {}::class.java.getResourceAsStream("/day15.input")?.bufferedReader()!!.readText()
    println(part1(input, 2000000))
    part2(input, 4000000).let {
        println("$it => ${it.first * 4000000L + it.second}")
    }
    println(System.currentTimeMillis() - now)
}