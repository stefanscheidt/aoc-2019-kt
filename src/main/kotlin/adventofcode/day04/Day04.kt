package adventofcode.day04


fun numberOfPasswordsInRange(
    rangeStart: String,
    rangeEnd: String,
    criteria: (String) -> Boolean
): Int =
    (rangeStart.toInt() until rangeEnd.toInt()).map(Int::toString).count(criteria)

fun criteriaOfPart1(password: String): Boolean =
    digitsDontDecrease(password) && twoAdjacentDigitsAreTheSame(password)

fun criteriaOfPart2(password: String): Boolean =
    digitsDontDecrease(password) && repetitionLenghtsOf(password).contains(2)

private fun digitsDontDecrease(password: String): Boolean =
    password.zipWithNext().all { it.first <= it.second }

private fun twoAdjacentDigitsAreTheSame(password: String): Boolean =
    password.zipWithNext().any() { it.first == it.second }

fun repetitionLenghtsOf(password: String): List<Int> {
    fun isRepetition(pair: Pair<Char, Char>) = pair.first == pair.second
    fun increaseLastOf(ints: List<Int>) = ints.dropLast(1) + (ints.last() + 1)

    return password
        .zipWithNext()
        .fold(listOf(1)) { lenghts, pair ->
            if (isRepetition(pair)) increaseLastOf(lenghts) else lenghts.plus(1)
        }
        .filterNot { it == 1 }
}

fun main() {
    println("Part 1: ${numberOfPasswordsInRange("171309", "643603", ::criteriaOfPart1)}")
    println("Part 2: ${numberOfPasswordsInRange("171309", "643603", ::criteriaOfPart2)}")
}