package com.bloidonia

import kotlin.streams.toList

private val input = """
    30373
    25512
    65332
    33549
    35390
""".trimIndent()

private data class Forest(val w: Int, val h: Int, val trees: List<Int>) {

    fun toCoord(offset: Int) = offset % w to offset / w

    fun height(x: Int, y: Int): Int {
        val index = (y * w) + x
        return trees[index]
    }

    fun visible(x: Int, y: Int): Boolean {
        val edge = x == 0 || y == 0 || x == w - 1 || y == h - 1
        if (edge) return true
        val th = height(x, y)
        return (0 until x).map { height(it, y) }.all { it < th } ||
                ((x + 1) until w).map { height(it, y) }.all { it < th } ||
                (0 until y).map { height(x, it) }.all { it < th } ||
                ((y + 1) until h).map { height(x, it) }.all { it < th }
    }

    fun scan(x: Int, y: Int, dx: Int, dy: Int): Int {
        var count = 0
        var cx = x + dx
        var cy = y + dy
        while (cx < w && cx >= 0 && cy < h && cy >= 0) {
            count++
            if (height(cx, cy) >= height(x, y)) {
                break
            }
            cx += dx
            cy += dy
        }
        return count
    }

    fun scenicScore(x: Int, y: Int): Int {
        val left = scan(x, y, -1, 0)
        val right = scan(x, y, 1, 0)
        val up = scan(x, y, 0, -1)
        val down = scan(x, y, 0, 1)
        val score = left * right * up * down
        return score
    }

    fun part1() =
        trees.withIndex().count {
            toCoord(it.index).let { (x, y) ->
                visible(x, y)
            }
        }

    fun part2() =
        trees.withIndex().maxOf {
            val toCoord = toCoord(it.index)
            val score = scenicScore(toCoord.first, toCoord.second)
            score
        }
}

private fun parse(input: String): Forest {
    val lines = input.lines()
    val w = lines[0].length
    val h = lines.size
    return Forest(w, h, lines.flatMap { it.toCharArray().map { it.digitToInt() }.toList() })
}

fun main() {
    println(parse(input).part1())
    println(parse(input).part2())

    val input = object {}::class.java.getResourceAsStream("/day08.input")?.bufferedReader()!!.readText()
    println(parse(input).part1())
    println(parse(input).part2())
}