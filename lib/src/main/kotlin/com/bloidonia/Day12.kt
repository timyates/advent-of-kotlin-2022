package com.bloidonia

import org.checkerframework.checker.units.qual.g

private val input = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
""".trimIndent()

private val alphabet = "abcdefghijklmnopqrstuvwxyz"

private fun lookup(c: Char) = if (c == 'S') 0 else if (c == 'E') 25 else alphabet.indexOf(c)
private fun parseLine(line: String) = line.map(::lookup)
private fun parse(input: String) = input.lines().map { parseLine(it) }
val POISON = -1 to -1

private fun walk(map: List<List<Int>>, w: Int, h: Int, s: Pair<Int, Int>, e: Pair<Int, Int>): Int {
    val possibles = { seen: Set<Pair<Int, Int>>, path: List<Pair<Int, Int>> ->
        val p = path.last()
        val (x, y) = p
        ((if (x > 0 && map[y][x - 1] <= map[y][x] + 1 && !seen.contains(x - 1 to y)) listOf(x - 1 to y) else listOf()) +
                (if (x < w - 1 && map[y][x + 1] <= map[y][x] + 1 && !seen.contains(x + 1 to y)) listOf(x + 1 to y) else listOf()) +
                (if (y > 0 && map[y - 1][x] <= map[y][x] + 1 && !seen.contains(x to y - 1)) listOf(x to y - 1) else listOf()) +
                (if (y < h - 1 && map[y + 1][x] <= map[y][x] + 1 && !seen.contains(x to y + 1)) listOf(x to y + 1) else listOf()))
            .ifEmpty { listOf(POISON) }
    }
    var paths = listOf(listOf(s))
    val complete = mutableListOf<List<Pair<Int, Int>>>()
    val seen = mutableSetOf(s)
    while(paths.isNotEmpty()) {
        paths = paths.flatMap { p -> possibles(seen, p).map {
            seen.add(it)
            p + it
        } }
        paths = paths.filter { !it.contains(POISON) }
        val done = paths.filter { it.last() == e }
        paths = paths.filter { it.last() != e }
        done.forEach {
            complete.add(it)
        }
    }
    return if (complete.isEmpty()) Int.MAX_VALUE else complete.minOf { it.size - 1 }
}

private fun part1(input: String) {
    val map = parse(input)
    val w = map[0].size
    val h = map.size
    val xy = { i: Int -> i % w to i / w }
    val (s, e) = input.lines().joinToString("").let {
        xy(it.indexOf('S')) to xy(it.indexOf('E'))
    }
    println(walk(map, w, h, s, e))
}

private fun part2(input: String) {
    val map = parse(input)
    val w = map[0].size
    val h = map.size
    val xy = { i: Int -> i % w to i / w }
    val res = input.lines().joinToString("").let {
        val e = xy(it.indexOf('E'))
        it.withIndex()
            .filter { it.value == 'a' || it.value == 'S' }
            .map { xy(it.index) to e }
            .minOf { (s, e) -> walk(map, w, h, s, e) }
    }
    println(res)
}
fun main() {
    part1(input)
    part2(input)
    val input = object {}::class.java.getResourceAsStream("/day12.input")?.bufferedReader()!!.readText()
    part1(input)
    part2(input)
}