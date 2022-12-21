package com.bloidonia

private val input = """
    2,2,2
    1,2,2
    3,2,2
    2,1,2
    2,3,2
    2,2,1
    2,2,3
    2,2,4
    2,2,6
    1,2,5
    3,2,5
    2,1,5
    2,3,5
""".trimIndent()

private data class Cube(val x: Int, val y: Int, val z: Int) {
    fun between(min: Cube, max:Cube): Boolean = x in min.x..max.x && y in min.y..max.y && z in min.z..max.z
}
private data class Scan(val cubes: Set<Cube>) {
    fun neighbours(cube: Cube) = sequence {
        yield(Cube(cube.x + 1, cube.y, cube.z))
        yield(Cube(cube.x - 1, cube.y, cube.z))
        yield(Cube(cube.x, cube.y + 1, cube.z))
        yield(Cube(cube.x, cube.y - 1, cube.z))
        yield(Cube(cube.x, cube.y, cube.z + 1))
        yield(Cube(cube.x, cube.y, cube.z - 1))
    }
    fun activeNeighbours(cube: Cube) = neighbours(cube).count { it in cubes }
    fun faces(cubes: Set<Cube> = this.cubes) = cubes.sumOf { 6 - activeNeighbours(it) }
    fun maxMin() = Cube(cubes.minOf { it.x - 1 }, cubes.minOf { it.y - 1 }, cubes.minOf { it.z -1 }) to Cube(cubes.maxOf { it.x + 1 }, cubes.maxOf { it.y + 1 }, cubes.maxOf { it.z + 1 })
    fun walk(maxMin: Pair<Cube, Cube>): Set<Cube> {
        val seen = mutableSetOf<Cube>()
        val found = mutableSetOf<Cube>()
        val todo = mutableSetOf(maxMin.first)
        while (todo.isNotEmpty()) {
            val cube = todo.first()
            todo.remove(cube)
            if (cube in seen) continue
            seen.add(cube)
            val neighbours = neighbours(cube).filter { it.between(maxMin.first, maxMin.second) && it !in seen }
            if (neighbours.any { it in cubes }) {
                found.add(cube)
            }
            todo.addAll(neighbours.filter { it !in cubes && it !in seen })
        }
        return found
    }
}

private fun churn(input: String) {
    val cubes = input
        .lineSequence()
        .map { it.split(Regex(","), 3).let { (x: String, y: String, z: String) -> Cube(x.toInt(), y.toInt(), z.toInt()) } }
        .toSet()
    val scan = Scan(cubes)
    println("Part 1: ${scan.faces()}")
    println("Part 2: ${scan.walk(scan.maxMin()).sumOf { scan.neighbours(it).count { it in cubes } }}")
}

fun main() {
    churn(input)
    val input = object {}::class.java.getResourceAsStream("/day18.input")?.bufferedReader()!!.readText()
    churn(input)
}