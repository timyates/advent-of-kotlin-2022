package com.bloidonia

import kotlin.math.max

private val input = """
    Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    Valve BB has flow rate=13; tunnels lead to valves CC, AA
    Valve CC has flow rate=2; tunnels lead to valves DD, BB
    Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
    Valve EE has flow rate=3; tunnels lead to valves FF, DD
    Valve FF has flow rate=0; tunnels lead to valves EE, GG
    Valve GG has flow rate=0; tunnels lead to valves FF, HH
    Valve HH has flow rate=22; tunnel leads to valve GG
    Valve II has flow rate=0; tunnels lead to valves AA, JJ
    Valve JJ has flow rate=21; tunnel leads to valve II
""".trimIndent()

private data class Valve(val name: String, val flow: Int, val tunnels: MutableList<String>)
private fun readValve(input: String): Valve {
    val (name, flow, tunnels) = Regex("Valve (\\S+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)").find(
        input
    )!!.destructured
    return Valve(name, flow.toInt(), tunnels.split(",").map { it.trim() }.toMutableList())
}
private fun readInput(input: String): Map<String, Valve> {
    return input
        .lineSequence()
        .map(::readValve)
        .associateBy { it.name }
}

private data class Path(val seen: List<Valve>, val flow: Int)
private class Cave(private val valves: Map<String, Valve>) {

    val switchable = valves.values.count { it.flow > 0 }
    val connections: Map<String, Map<String, Int>> = valves.values.map {
        it.name to it.tunnels.map { t -> t to 1 }.toMap().toMutableMap()
    }.toMap().let { c ->
        c.keys.forEach { k ->
            c.keys.forEach { i ->
                c.keys.forEach { j ->
                    val iToK = c[i]?.get(k) ?: 1000
                    val kToJ = c[k]?.get(j) ?: 1000
                    val iToJ = c[i]?.get(j) ?: 1000
                    if (iToK + kToJ < iToJ) {
                        c[i]?.set(j, iToK + kToJ)
                    }
                }
            }
        }
        c.values.forEach {
            it.filter { valves[it.key]!!.flow == 0 }.keys.forEach { k ->
                it.remove(k)
            }
        }
        c
    }

    fun part1(): Int {
        var paths = listOf(Path(listOf(valves["AA"]!!), 0))
        var tick = 1
        while (tick <= 30) {
            for (path in paths) {
                if (path.seen.size == switchable) {
                    continue
                }
                val last = path.seen.last()
                connections[last.name]?.forEach { (name, flow) ->
                    paths += Path(path.seen + valves[name]!!, path.flow + flow)
                }
            }
        }
        return connections["AA"]!!["HH"]!!
    }

    override fun toString(): String = connections.toString()
}

private fun part1(valves: Map<String, Valve>) {
    val cave = Cave(valves)
    println(cave)
}

fun main() {
    part1(readInput(input))
}