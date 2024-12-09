private const val DAY = "Day09"

fun main() {
    fun part1(input: String): Int {
        return 0
    }


    fun part2(input: String): Int {
        return 0
    }

//    val testInput = readInput("${DAY}_test")
    val testInput = "2333133121414131402"
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInputAsString(DAY)
    part1(input).println()
    part2(input).println()
}
