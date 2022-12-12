package com.bloidonia

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

private class Map(val input: String) {
    val map = parse(input)
    val w = map[0].size
    val h = map.size
    val xy = { i: Int -> i % w to i / w }
}

private fun walk(map: Map, s: Pair<Int, Int>, e: Pair<Int, Int>): Int {
    val possibles = { seen: Set<Pair<Int, Int>>, path: List<Pair<Int, Int>> ->
        val p = path.last()
        val (x, y) = p
        ((if (x > 0 && map.map[y][x - 1] <= map.map[y][x] + 1 && !seen.contains(x - 1 to y)) listOf(x - 1 to y) else listOf()) +
                (if (x < map.w - 1 && map.map[y][x + 1] <= map.map[y][x] + 1 && !seen.contains(x + 1 to y)) listOf(x + 1 to y) else listOf()) +
                (if (y > 0 && map.map[y - 1][x] <= map.map[y][x] + 1 && !seen.contains(x to y - 1)) listOf(x to y - 1) else listOf()) +
                (if (y < map.h - 1 && map.map[y + 1][x] <= map.map[y][x] + 1 && !seen.contains(x to y + 1)) listOf(x to y + 1) else listOf()))
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
    val map = Map(input)
    val (s, e) = input.lines().joinToString("").let {
        map.xy(it.indexOf('S')) to map.xy(it.indexOf('E'))
    }
    println(walk(map, s, e))
}

private fun part2(input: String) {
    val map = Map(input)
    val res = input.lines().joinToString("").let {
        val e = map.xy(it.indexOf('E'))
        it.withIndex()
            .filter { it.value == 'a' || it.value == 'S' }
            .map { map.xy(it.index) to e }
            .minOf { (s, e) -> walk(map, s, e) }
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