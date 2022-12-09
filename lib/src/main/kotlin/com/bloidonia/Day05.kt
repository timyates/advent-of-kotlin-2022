package com.bloidonia

import java.util.Stack
import java.util.regex.Pattern

private val input = """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
"""

fun parseStacks(input: String): List<Stack<Char>> {
    val stacks = input.split("\n").last().trim().split(Regex("\\s+")).last().toInt()
    val piles = (1..stacks).map { Stack<Char>() }
    input.split("\n").dropLast(1).reversed().forEach { line ->
        (0 until stacks).forEach { pile ->
            val pos = pile * 4 + 1
            if (pos < line.length) {
                val c = line[pos]
                if (c != ' ') piles[pile].push(c)
            }
        }
    }
    return piles
}

fun split(input: String): Pair<String, String> = input.split("\n\n", limit = 2).let { Pair(it[0], it[1]) }

fun processStacks(stacks: List<Stack<Char>>, commands: String, part1: Boolean = true) {
    commands.split("\n").forEach { line ->
        Pattern.compile("move (\\d+) from (\\d+) to (\\d+)").matcher(line).let { matcher ->
            if (matcher.matches()) {
                val (count, from, to) = listOf<Int>(
                    matcher.group(1).toInt(),
                    matcher.group(2).toInt() - 1,
                    matcher.group(3).toInt() - 1
                )
                if (part1) {
                    (1..count).forEach {
                        stacks[to].push(stacks[from].pop())
                    }
                } else {
                    (1..count).map { stacks[from].pop() }.reversed().forEach {
                        stacks[to].push(it)
                    }
                }
            }
        }
    }
}

fun main() {
    val (a, b) = split(input)
    val stacks = parseStacks(a)
    processStacks(stacks, b)
    println(stacks.map { it.peek() }.joinToString(""))
    val stacks_ = parseStacks(a)
    processStacks(stacks_, b, false)
    println(stacks_.map { it.peek() }.joinToString(""))

    val input = object {}::class.java.getResourceAsStream("/day05.input")?.bufferedReader()!!.readText()
    val (c, d) = split(input)
    val stacks2 = parseStacks(c)
    processStacks(stacks2, d)
    println(stacks2.map { it.peek() }.joinToString(""))

    val stacks3 = parseStacks(c)
    processStacks(stacks3, d, false)
    println(stacks3.map { it.peek() }.joinToString(""))
}

