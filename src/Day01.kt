import kotlin.math.absoluteValue

fun main() {
    fun parseData(input: List<String>): Pair<List<Int>, List<Int>> {
        val data = input.map { line ->
            val ints = line.splitMultipleSpaces()
            ints[0].toInt() to ints[1].toInt()
        }.unzip()

        val sort1 = data.first.sorted()
        val sort2 = data.second.sorted()

        return sort1 to sort2
    }

    fun part1(input: List<String>): Int {
        val data = parseData(input)

        val list = data.first.zip(data.second)
        return list.sumOf { (it.first - it.second).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val data = parseData(input)

        val left = data.first
        val right = data.second

        return left.sumOf { number ->
            val repeat = right.count { it == number }
            number * repeat
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
