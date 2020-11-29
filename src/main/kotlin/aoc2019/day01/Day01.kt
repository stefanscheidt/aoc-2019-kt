package aoc2019.day01

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.math.max


typealias Mass = Int

typealias Fuel = Int

internal fun processLines(lines: Sequence<String>): Int {
    val masses = lines.map { it.toInt() }
    return fuelForModulesWithMasses(masses)
}

internal fun fuelForModulesWithMasses(masses: Sequence<Mass>): Fuel =
    masses.map { fuelForModuleWithMass(it) }.sum()

internal fun fuelForModuleWithMass(mass: Mass): Fuel =
    fuelForFuel(fuelForMass(mass)).map { it }.sum()

private fun fuelForMass(mass: Mass): Fuel =
    max((mass / 3) - 2, 0)

private fun fuelForFuel(seed: Fuel): Sequence<Mass> =
    generateSequence(seed) { zeroToNull(fuelForMass(it)) }

private fun zeroToNull(value: Int): Int? =
    if (value == 0) null else value

fun main() {
    val result = File("./input/day01.txt").useLines(UTF_8, ::processLines)
    println(result)
}
