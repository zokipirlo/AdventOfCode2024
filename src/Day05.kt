private const val DAY = "Day05"

fun main() {
    fun part1(input: List<String>): Int {
        return 0
    }


    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
