package aoc2019.util

private const val LOG_ENABLED = false

internal fun log(obj: Any, msg: String) {
    if (LOG_ENABLED) {
        println("[${obj::class.java.simpleName} - ${Thread.currentThread().name}] $msg")
    }
}