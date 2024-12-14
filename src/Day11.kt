import utils.println
import utils.readInputAsString

private const val DAY = "Day11"

fun main() {
    fun blinkOnce(stones: List<Long>) = buildList {
        stones.forEach { stone ->
            when {
                stone == 0L -> add(1L)
                stone.toString().length % 2 == 0 -> {
                    val stoneString = stone.toString()
                    val (left, right) = stoneString.chunked(stoneString.length / 2)
                    add(left.toLong())
                    add(right.toLong())
                }

                else -> {
                    val newValue = stone * 2024
                    add(newValue)
                }
            }
        }
    }

    fun blink(total: Int, initialStones: List<Long>): Long {
        var stones = listOf(initialStones)
        repeat(total) {
            println("Repeat $it")
            stones = stones.flatMap { stoneItem ->
                val newStoneItems = blinkOnce(stoneItem)
                newStoneItems.chunked(1000)
            }
        }
        return stones.sumOf { it.size.toLong() }
    }

    fun part1(input: String): Long {
        var stones = input.split(" ").map { it.toLong() }
        return blink(25, stones)
    }


    fun part2(input: String): Long {
        var stones = input.split(" ").map { it.toLong() }
        return blink(75, stones)
    }

    val testInput = readInputAsString("${DAY}_test")
    check(part1(testInput) == 55312L)

    val input = readInputAsString(DAY)
    check(part1(input) == 199753L)
    part1(input).println()
    part2(input).println()
}
