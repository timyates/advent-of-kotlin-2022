package com.bloidonia

private val input = """
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
""".trimIndent()

fun scan(input: String, top: Int = 1) =
    input.split("\n\n").map { it.split("\n") }.map { it.map { it.toInt() }.sum() }.sortedDescending().take(top).sum()

fun main() {
    println(scan(input))
    val input = object {}::class.java.getResourceAsStream("/day01.input")?.bufferedReader()!!.readText()
    println(scan(input))
    println(scan(input, 3))
}

