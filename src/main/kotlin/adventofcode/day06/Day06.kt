package adventofcode.day06

import java.io.File


fun main() {
    val spaceMap = File("./input/day06.txt").readText()
    println(orbitsOf(parseSpaceMap(spaceMap)))
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

// --- Map

typealias SpaceMap = Set<ObjectInSpace>

internal fun orbitsOf(objectsInSpace: SpaceMap) =
    objectsInSpace.map(ObjectInSpace::numberOfOrbits).sum()

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
