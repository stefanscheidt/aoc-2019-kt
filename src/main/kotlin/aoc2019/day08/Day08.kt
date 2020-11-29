package aoc2019.day08

import java.io.File


typealias Layer = List<List<Int>>
typealias Image = List<Layer>

fun String.toImage(width: Int, height: Int): Image =
    this.map { it.toInt() - '0'.toInt() }
        .chunked(width)
        .chunked(height)

fun digitCount(layer: Layer, digit: Int): Int =
    layer.flatten().count { it == digit }

fun layerWithFewestDigit(layers: List<Layer>, digit: Int): Layer? =
    layers
        .associateWith { digitCount(it, digit) }
        .minByOrNull { it.value }
        ?.key

fun render(image: Image, width: Int, height: Int): String =
    image
        .map { it.flatten() }
        .fold(List(width * height) { 2 }) { acc, layer ->
            acc.zip(layer).map { (a, l) -> if (a == 2) l else a }
        }
        .map { if (it == 0) ' ' else '*' }
        .chunked(width)
        .joinToString(separator = System.lineSeparator()) {
            it.joinToString(separator = "")
        }


fun main() {
    val input = File("./input/day08.txt").readText()
    val image = input.toImage(25, 6)

    val layer = layerWithFewestDigit(image, 0)!!
    println("Part 1: ${digitCount(layer, 1) * digitCount(layer, 2)}")

    println(render(image, 25, 6))
}