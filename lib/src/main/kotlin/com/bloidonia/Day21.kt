package com.bloidonia

private val input = """
    root: pppw + sjmn
    dbpl: 5
    cczh: sllz + lgvd
    zczc: 2
    ptdq: humn - dvpt
    dvpt: 3
    lfqf: 4
    humn: 5
    ljgn: 2
    sjmn: drzm * dbpl
    sllz: 4
    pppw: cczh / lfqf
    lgvd: ljgn * ptdq
    drzm: hmdt - zczc
    hmdt: 32
""".trimIndent()

private sealed class Primate(open val name: String) {
    data class Number(override val name: String, val value: Int) : Primate(name)
    data class Operation(override val name: String, val op: String, val left: String, val right: String) : Primate(name)
}

private fun parseLine(line: String): Primate {
    return Regex("([a-z]+): (?:(\\d+)|([a-z]+) ([+*/-]) ([a-z]+))").find(line)?.let {
        val name = it.groups[1]!!.value
        val num = it.groups[2]?.value
        val left = it.groups[3]?.value
        val op = it.groups[4]?.value
        val right = it.groups[5]?.value
        if (op == null) {
            Primate.Number(name, num!!.toInt())
        } else {
            Primate.Operation(name, op, left!!, right!!)
        }
    } ?: throw IllegalArgumentException("Invalid line: $line")
}

private fun parse(input: String) = input.lines().map(::parseLine).associateBy { it.name }

private fun getValue(tree: Map<String, Primate>, primate: Primate): Long = when {
        primate is Primate.Number -> primate.value.toLong()
        primate is Primate.Operation -> {
            val left = getValue(tree, tree[primate.left]!!)
            val right = getValue(tree, tree[primate.right]!!)
            when (primate.op) {
                "+" -> left + right
                "-" -> left - right
                "*" -> left * right
                "/" -> left / right
                else -> throw IllegalArgumentException("Unknown op: ${primate.op}")
            }
        }
        else -> throw IllegalArgumentException("Unknown primate: $primate")
    }

private fun getValue2(tree: Map<String, Primate>, primate: Primate, humn: Long): Double = when {
    primate is Primate.Number -> {
        if (primate.name == "humn") humn.toDouble() else primate.value.toDouble()
    }
    primate is Primate.Operation -> {
        val left = getValue2(tree, tree[primate.left]!!, humn)
        val right = getValue2(tree, tree[primate.right]!!, humn)
        if (primate.name == "root") {
            println("${left == right} (${left - right})")
        }
        when (primate.op) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> left / right
            else -> throw IllegalArgumentException("Unknown op: ${primate.op}")
        }
    }
    else -> throw IllegalArgumentException("Unknown primate: $primate")
}

private fun walk(input: String) {
    parse(input).let {
        println(getValue(it, it["root"]!!))
    }
}
private fun walk2(input: String, humn: Long) {
    println("humn: $humn")
    parse(input).let {
        getValue2(it, it["root"]!!, humn)
    }
}

fun main() {
    walk(input)
    walk2(input, 0)
    walk2(input, 301)
    val input = object {}::class.java.getResourceAsStream("/day21.input")?.bufferedReader()!!.readText()
    walk(input)
    walk2(input, 0)
    walk2(input, 3876578844368)
    walk2(input, 3876879264207)
    walk2(input, 3876903491613)
    walk2(input, 3876907167495)
}