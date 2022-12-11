package com.bloidonia

private val input = """
    Monkey 0:
      Starting items: 79, 98
      Operation: new = old * 19
      Test: divisible by 23
        If true: throw to monkey 2
        If false: throw to monkey 3
    
    Monkey 1:
      Starting items: 54, 65, 75, 74
      Operation: new = old + 6
      Test: divisible by 19
        If true: throw to monkey 2
        If false: throw to monkey 0
    
    Monkey 2:
      Starting items: 79, 60, 97
      Operation: new = old * old
      Test: divisible by 13
        If true: throw to monkey 1
        If false: throw to monkey 3
    
    Monkey 3:
      Starting items: 74
      Operation: new = old + 3
      Test: divisible by 17
        If true: throw to monkey 0
        If false: throw to monkey 1
""".trimIndent()

data class Monkey(val items: MutableList<Long>, val op: (Long) -> Long, val choice: (Long) -> Int, val part2: Long) {

    var inspected: Long = 0L;

    private fun bored(value: Long, part2: Boolean, product: Long): Long = if (part2) {
        value % product
    } else value.div(3)

    private fun throwTo(monkey: Monkey, part2: Boolean, product: Long) {
        val item = items.removeAt(0)
        monkey.items.add(bored(op(item), part2, product))
    }

    fun turn(monkeys: List<Monkey>, part2: Boolean, product: Long) {
        while (items.isNotEmpty()) {
            inspected++;
            throwTo(monkeys[choice(bored(op(items[0]), part2, product))], part2, product)
        }
    }

    override fun toString(): String {
        return "Monkey(${inspected} : $items)"
    }
}

private fun parseMonkey(input: String): Monkey {
    val lines = input.split("\n")
    val items = lines[1].split(":")[1].split(",").map { it.trim().toLong() }.toMutableList()
    val op = lines[2].split("=")[1].let {
        it.split(" ").let { parts ->
            when (parts[2]) {
                "*" -> { x: Long -> x * if (parts[3] == "old") x else parts[3].toLong() }
                "+" -> { x: Long -> x + if (parts[3] == "old") x else parts[3].toLong() }
                else -> throw IllegalArgumentException("Unknown op: ${parts[2]}")
            }
        }
    }
    val part2 = lines[3].split("by")[1].trim().toLong()
    val choice = { x: Long ->
        if (x % part2 == 0L)
            lines[4].split("monkey")[1].trim().toInt()
        else
            lines[5].split("monkey")[1].trim().toInt()
    }
    return Monkey(items, op, choice, part2)
}

private fun parse(input: String) = input.split("\n\n").map(::parseMonkey)

private fun round(monkeys: List<Monkey>, part2: Boolean, product: Long) = monkeys.apply { forEach { m -> m.turn(monkeys, part2, product) } }

private fun run(monkeys: List<Monkey>, rounds: Int, part2: Boolean): Long {
    val product = monkeys.map { it.part2 }.reduce(Long::times)
    (1..rounds).forEach { round(monkeys, part2, product) }
    return monkeys.map { it.inspected }.sortedByDescending { it }.take(2).reduce(Long::times)
}

fun main() {
    println(run(parse(input), 20, false))
    println(run(parse(input), 10000, true))
    val input = object {}::class.java.getResourceAsStream("/day11.input")?.bufferedReader()!!.readText()
    println(run(parse(input), 20, false))
    println(run(parse(input), 10000, true))
}