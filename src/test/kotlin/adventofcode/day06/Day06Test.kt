package adventofcode.day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

@Suppress("GrazieInspection")
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

@Suppress("GrazieInspection")
private val example02 = """
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
    K)YOU
    I)SAN
""".trimIndent()

val com = ObjectInSpace("COM")

class Day06Test {

    @Test
    fun `should compute orbits for input`() {
        val spaceMap = parseSpaceMap(File("./input/day06.txt").readText())
        assertThat(spaceMap.numberOfOrbits()).isEqualTo(224901)
    }

    @Test
    fun `shoud compute total number of orbits for example 1`() {
        assertThat(parseSpaceMap(example01).numberOfOrbits()).isEqualTo(42)
    }

    @Test
    fun `number of orbits of com is zero`() {
        assertThat(setOf(com).numberOfOrbits()).isEqualTo(0)
    }

    @Test
    fun `number of orbits of com with one satellite is one`() {
        val a = ObjectInSpace("A", com)
        assertThat(setOf(com, a).numberOfOrbits()).isEqualTo(1)
    }

    @Test
    fun `number of orbits of com with some satellites`() {
        val a = ObjectInSpace("A", com)
        val b = ObjectInSpace("B", com)
        val c = ObjectInSpace("C", b)
        assertThat(setOf(com, a, b, c).numberOfOrbits()).isEqualTo(4)
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

    @Test
    fun `path to object`() {
        val a = ObjectInSpace("A", com)
        val b = ObjectInSpace("B", a)
        val c = ObjectInSpace("C", b)
        val d = ObjectInSpace("D", a)

        assertThat(pathTo(com)).isEmpty()
        assertThat(pathTo(a)).isEqualTo(listOf(com))
        assertThat(pathTo(c)).isEqualTo(listOf(com, a, b))
        assertThat(pathTo(d)).isEqualTo(listOf(com, a))

        assertThat(pathTo(ObjectInSpace("X"))).isEmpty()
    }

    @Test
    fun `number of orbital transfers`() {
        val spaceMap = parseSpaceMap(example02)
        assertThat(spaceMap.numberOfOrbitalTransfers("YOU" to "SAN")).isEqualTo(4)
    }

    @Test
    fun `should compute number of orbital transfers for input`() {
        val spaceMap = parseSpaceMap(File("./input/day06.txt").readText())
        assertThat(spaceMap.numberOfOrbitalTransfers("YOU" to "SAN")).isEqualTo(334)
    }

}