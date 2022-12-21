package com.bloidonia

private val input = """
    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,4],4,4]
    [[4,4],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
""".trimIndent()

private sealed class Element {
    data class Array(val elements: MutableList<Element>) : Element() {
        override fun toString(): String = "[${elements.joinToString(",")}]"
    }
    data class Number(val value: Int) : Element() {
        override fun toString(): String = value.toString()
    }
}

private fun parseElement(input: String): Element {
    val root = Element.Array(mutableListOf())
    val stack = mutableListOf<Element>()
    var current: Element = root
    var idx = 1
    while (idx < input.length - 1) {
        if (input[idx] == '[') {
            val array = Element.Array(mutableListOf())
            (current as Element.Array).elements.add(array)
            stack.add(current)
            current = array
            idx++
        } else if (input[idx] == ']') {
            current = stack.removeAt(stack.size - 1)
            idx++
        } else if (input[idx] == ',') {
            idx++
        } else {
            val number = input.substring(idx).takeWhile { it.isDigit() }
            current as Element.Array
            current.elements.add(Element.Number(number.toInt()))
            idx += number.length
        }
    }
    return root
}

private fun parsePair(input: String) = input.split("\n", limit = 2).map(::parseElement).let { (a, b) -> a to b }
private fun parseInput(input: String) = input.split("\n\n").map(::parsePair)

private fun compare(left: Element, right: Element): Int {
    if (left is Element.Array && right is Element.Array) {
        var idx = 0;
        while (idx < left.elements.size && idx < right.elements.size) {
            val result = compare(left.elements[idx], right.elements[idx])
            if (result != 0) return result
            idx++
        }
        if (idx < left.elements.size) return 1
        if (idx < right.elements.size) return -1
    }
    if (left is Element.Number && right is Element.Array) {
        return compare(Element.Array(mutableListOf(left)), right)
    }
    if (left is Element.Array && right is Element.Number) {
        return compare(left, Element.Array(mutableListOf(right)))
    }
    if (left is Element.Number && right is Element.Number) {
        return left.value.compareTo(right.value)
    }
    return 0
}

private fun part1(input: List<Pair<Element, Element>>) = input.map { p -> compare(p.first, p.second) }.withIndex().filter { it.value <= 0 }.sumOf { it.index + 1 }
private fun part2(input: List<Pair<Element, Element>>): Int {
    val two = Element.Array(mutableListOf(Element.Array(mutableListOf(Element.Number(2)))))
    val six = Element.Array(mutableListOf(Element.Array(mutableListOf(Element.Number(6)))))
    input.fold(mutableListOf<Element>(two, six)) { acc, p -> acc.apply { add(p.first); add(p.second) } }.sortedWith(Comparator(::compare)).let {
        return (it.indexOf(two) + 1) * (it.indexOf(six) + 1)
    }
}

fun main() {
    println(part1(parseInput(input)))
    println(part2(parseInput(input)))
    val input = object {}::class.java.getResourceAsStream("/day13.input")?.bufferedReader()!!.readText()
    println(part1(parseInput(input)))
    println(part2(parseInput(input)))
}