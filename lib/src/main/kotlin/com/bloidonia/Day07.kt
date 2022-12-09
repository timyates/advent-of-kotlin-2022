package com.bloidonia

import java.net.URI
import java.nio.file.Path


private val input = """
    ${'$'} cd /
    ${'$'} ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    ${'$'} cd a
    ${'$'} ls
    dir e
    29116 f
    2557 g
    62596 h.lst
    ${'$'} cd e
    ${'$'} ls
    584 i
    ${'$'} cd ..
    ${'$'} cd ..
    ${'$'} cd d
    ${'$'} ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
""".trimIndent()

private interface Node {
    val parent: Node?
    val name: String
    val children: Map<String, Node>

    fun size(): Long
}

private data class Dir(override val parent: Node?, override val name: String, override val children: MutableMap<String, Node>) : Node {
    override fun size(): Long =
        children.map { it.value.size() }.sum()

    override fun toString(): String {
        return "Dir(size=${size()}, name='$name', children=${children.keys})"
    }
}

private data class File(override val parent: Node?, override val name: String, val size: Int) : Node {
    override val children: Map<String, Node> = emptyMap()
    override fun size(): Long = size.toLong()
}

private fun part1(input: String) {
    val root = Dir(null, "/", mutableMapOf())
    var ptr: Node = root
    var pos = Path.of("/")
    val dirList = mutableListOf<Dir>(root)

    input.split("\n").forEach {
        when {
            it.startsWith("\$ cd ") -> {
                val path = it.substring(5)
                pos = pos.resolve(path).normalize()

                if (path.equals("/")) {
                    pos = Path.of("/")
                    ptr = root
                } else if (path.equals("..")) {
                    pos = pos.parent
                    ptr = ptr.parent!!
                } else {
                    ptr = ptr.children[path]!!
                    pos = pos.resolve(path)
                }
            }
            it.startsWith("$ ls") -> {
                // Nowt
            }
            it.startsWith("dir ") -> {
                val dir = Dir(ptr, it.substring(4), mutableMapOf())
                dirList.add(dir)
                (ptr as Dir).children.put(it.substring(4), dir)
            }
            else -> {
                it.split(" ").let { (size, name) ->
                    (ptr as Dir).children.put(name, File(ptr, name, size.toInt()))
                }
            }
        }
    }
    println(dirList.filter { it.size() < 100000 }.sumOf { it.size() })

    val reqd = 30000000 - (70000000 - root.size())
    println("Need to free up: $reqd")
    println(dirList.filter { it.size() >= reqd }.sortedBy { it.size() }.first())
}

fun main() {
    part1(input)
    val input = object {}::class.java.getResourceAsStream("/day07.input")?.bufferedReader()!!.readText()
    part1(input)

}