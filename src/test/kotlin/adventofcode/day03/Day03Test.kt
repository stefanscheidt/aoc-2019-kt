package adventofcode.day03

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File


class TraceTest {

    @Test
    fun `compute trace`() {
        assertThat(traceOf("U1"))
            .containsExactly(Point(0, 1))
        assertThat(traceOf("U2,R2"))
            .containsExactly(Point(0, 1), Point(0, 2), Point(1, 2), Point(2, 2))
    }

    @Test
    fun `distance of nearest intersection`() {
        val tr11 = traceOf("R8,U5,L5,D3")
        val tr12 = traceOf("U7,R6,D4,L4")
        assertThat(distanceToNearestIntersectionOf(tr11, tr12)).isEqualTo(6)

        val tr21 = traceOf("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val tr22 = traceOf("U62,R66,U55,R34,D71,R55,D58,R83")
        assertThat(distanceToNearestIntersectionOf(tr21, tr22)).isEqualTo(159)

        val tr31 = traceOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val tr32 = traceOf("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        assertThat(distanceToNearestIntersectionOf(tr31, tr32)).isEqualTo(135)
    }

    @Test
    fun `solution of part 1`() {
        val lines = File("./input/day03.txt").readLines()
        val tr1 = traceOf(lines[0])
        val tr2 = traceOf(lines[1])
        assertThat(distanceToNearestIntersectionOf(tr1, tr2)).isEqualTo(1674)
    }

    @Test
    fun `steps to point in trace`() {
        val trace = traceOf("R8,U5,L5,D3")
        assertThat(stepsToPoint(Point(3, 3), trace)).isEqualTo(20)
    }

    @Test
    fun `minimal wire steps`() {
        val tr11 = traceOf("R8,U5,L5,D3")
        val tr12 = traceOf("U7,R6,D4,L4")
        assertThat(minimalWireSteps(tr11, tr12)).isEqualTo(30)

        val tr21 = traceOf("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val tr22 = traceOf("U62,R66,U55,R34,D71,R55,D58,R83")
        assertThat(minimalWireSteps(tr21, tr22)).isEqualTo(610)

        val tr31 = traceOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val tr32 = traceOf("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        assertThat(minimalWireSteps(tr31, tr32)).isEqualTo(410)
    }

    @Test
    fun `solution of part 2`() {
        val lines = File("./input/day03.txt").readLines()
        val tr1 = traceOf(lines[0])
        val tr2 = traceOf(lines[1])
        assertThat(minimalWireSteps(tr1, tr2)).isEqualTo(14012)
    }

}