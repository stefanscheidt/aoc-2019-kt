package adventofcode.day06

import java.io.File


fun main() {
    val spaceMap = parseSpaceMap(File("./input/day06.txt").readText())
    println("Part 1: ${spaceMap.numberOfOrbits()}")
    println("Part 2: ${spaceMap.numberOfOrbitalTransfers("YOU" to "SAN")}")
}

// --- Objects in Space

data class ObjectInSpace(
    val name: String,
    val center: ObjectInSpace? = null
) {
    fun numberOfOrbits(): Int =
        if (center == null) 0 else 1 + center.numberOfOrbits()

    override fun toString(): String =
        "${center?.name ?: "/"})$name"
}

// --- Path

typealias Path = List<ObjectInSpace>

fun pathTo(objectInSpace: ObjectInSpace): Path =
    generateSequence(objectInSpace.center, ObjectInSpace::center).toList().reversed()

// --- Map

typealias SpaceMap = Set<ObjectInSpace>

fun SpaceMap.numberOfOrbits(): Int =
    this.map(ObjectInSpace::numberOfOrbits).sum()

fun SpaceMap.numberOfOrbitalTransfers(startToDestination: Pair<String, String>): Int {
    fun find(name: String): ObjectInSpace {
        val obj = this.find { it.name == name } ?: throw IllegalArgumentException("$name not found in map")
        if (obj.center == null) throw IllegalArgumentException("$name has no center.")
        return obj
    }

    val pathToStart = pathTo(find(startToDestination.first))
    val pathToDestination = pathTo(find(startToDestination.second))
    val indexOfLastSharedObject = pathToStart.zip(pathToDestination).count { it.first == it.second }

    return (pathToStart.size - indexOfLastSharedObject) + (pathToDestination.size - indexOfLastSharedObject)
}

// --- Parse Space Map

fun parseSpaceMap(spaceMap: String, centerName: String = "COM"): SpaceMap = MapParser(spaceMap, centerName).parse()

internal class MapParser(spaceMap: String, private val centerName: String = "COM") {

    private val objects = spaceMap.lines()
        .map { it.split(")") }
        .associate { it[1] to it[0] }

    fun parse(): SpaceMap =
        objectsWithCenter(centerName)

    private fun objectsWithCenter(name: String, center: ObjectInSpace? = null): SpaceMap {
        val thisObject = ObjectInSpace(name, center = center)
        val nextObjects = objects.filterValues { it == name }.keys
        return if (nextObjects.isEmpty())
            setOf(thisObject)
        else
            setOf(thisObject) + nextObjects.flatMap { objectsWithCenter(it, thisObject) }.toSet()
    }

}
