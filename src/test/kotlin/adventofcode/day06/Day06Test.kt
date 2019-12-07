package adventofcode.day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

private val example01 = """
    COM)B
    B)C
    C)D
    D)E
    E)F
    B)G
    G)H
    D)I
    E)J
    J)K
    K)L
    """.trimIndent()

val com = ObjectInSpace("COM")

class Day06Test {

    @Test
    fun `should comput orbits for input`() {
        val spaceMap = File("./input/day06.txt").readText()
        assertThat(orbitsOf(parseSpaceMap(spaceMap))).isEqualTo(224901)
    }

    @Test
    fun `shoud compute total number of orbits for example 1`() {
        assertThat(orbitsOf(parseSpaceMap(example01))).isEqualTo(42)
    }

    @Test
    fun `number of orbits of com is zero`() {
        assertThat(orbitsOf(setOf(com))).isEqualTo(0)
    }

    @Test
    fun `number of orbits of com with one satellite is one`() {
        val a = ObjectInSpace("A", com)
        assertThat(orbitsOf(setOf(com, a))).isEqualTo(1)
    }

    @Test
    fun `number of orbits of com with some satellites`() {
        val a = ObjectInSpace("A", com)
        val b = ObjectInSpace("B", com)
        val c = ObjectInSpace("C", b)
        assertThat(orbitsOf(setOf(com, a, b, c))).isEqualTo(4)
    }

    @Test
    fun `parse space map with some objects`() {
        val a = ObjectInSpace("A", com)
        val b = ObjectInSpace("B", a)
        val c = ObjectInSpace("C", b)
        val d = ObjectInSpace("D", a)
        val spaceMap = """
            ${com.name})${a.name}
            ${a.name})${b.name}
            ${b.name})${c.name}
            ${a.name})${d.name}
        """.trimIndent()
        assertThat(parseSpaceMap(spaceMap)).containsExactlyInAnyOrder(com, a, b, c, d)
    }

}