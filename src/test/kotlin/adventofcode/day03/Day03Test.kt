package adventofcode.day03

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File


class TraceTest {

    @Test
    fun `compute trace`() {
        assertThat("U1".trace())
            .containsExactly(Point(0, 0), Point(0, 1))
        assertThat("U2,R2".trace())
            .containsExactly(Point(0, 0), Point(0, 1), Point(0, 2), Point(1, 2), Point(2, 2))
    }

    @Test
    fun `distance of nearest intersection`() {
        val tr11 = "R8,U5,L5,D3".trace()
        val tr12 = "U7,R6,D4,L4".trace()
        assertThat(distToNearestIntersect(tr11, tr12)).isEqualTo(6)

        val tr21 = "R75,D30,R83,U83,L12,D49,R71,U7,L72".trace()
        val tr22 = "U62,R66,U55,R34,D71,R55,D58,R83".trace()
        assertThat(distToNearestIntersect(tr21, tr22)).isEqualTo(159)

        val tr31 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51".trace()
        val tr32 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7".trace()
        assertThat(distToNearestIntersect(tr31, tr32)).isEqualTo(135)
    }

    @Test
    fun `solution of part 1`() {
        val lines = File("./input/day03.txt").readLines()
        val tr1 = lines[0].trace()
        val tr2 = lines[1].trace()
        assertThat(distToNearestIntersect(tr1, tr2)).isEqualTo(1674)
    }

}