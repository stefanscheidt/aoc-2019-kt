package adventofcode.day01

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.math.max


inline class Mass(val value: Int) {
    fun toInt(): Int = value
}

fun Int.toMass(): Mass = Mass(this)

inline class Fuel(val value: Int) {
    fun toInt(): Int = value
    fun asMass(): Mass = Mass(value)
}

fun Int.toFuel(): Fuel = Fuel(this)

internal fun processLines(lines: Sequence<String>): Int {
    val masses = lines.map { it.toInt().toMass() }
    return fuelForModulesWithMasses(masses).toInt()
}

internal fun fuelForModulesWithMasses(masses: Sequence<Mass>): Fuel =
    Fuel(masses.map { fuelForModuleWithMass(it).toInt() }.sum())

internal fun fuelForModuleWithMass(mass: Mass): Fuel =
    fuelForFuel(fuelForMass(mass)).map { it.toInt() }.sum().toFuel()

private fun fuelForMass(mass: Mass): Fuel =
    Fuel(max((mass.value / 3) - 2, 0))

private fun fuelForFuel(seed: Fuel): Sequence<Mass> =
    generateSequence(seed.asMass()) { fuelForMass(it).asMass().zeroToNull() }

private fun Mass.zeroToNull(): Mass? =
    if (this.toInt() == 0) null else this

fun main() {
    val result = File("./input/day01.txt").useLines(UTF_8, ::processLines)
    println(result)
}
