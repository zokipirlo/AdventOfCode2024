import utils.println
import utils.readInputAsString

private const val DAY = "Day11"

fun main() {
    class StoneList(input: String) {
        val stones = input.split(" ").map { it.toLong() }

        private fun blinkOnce(stonesCount: Map<Long, Long>) = buildMap<Long, Long>{
            stonesCount.forEach { (stone, count) ->
                when {
                    stone == 0L -> merge(1L, count, Long::plus)
                    stone.toString().length % 2 == 0 -> {
                        val stoneString = stone.toString()
                        val (left, right) = stoneString.chunked(stoneString.length / 2)

                        merge(left.toLong(), count, Long::plus)
                        merge(right.toLong(), count, Long::plus)
                    }

                    else -> {
                        val newValue = stone * 2024
                        merge(newValue, count, Long::plus)
                    }
                }
            }
        }

        fun blinkAll(repeats: Int): Long {
            var stoneIteration: Map<Long, Long> = stones.groupingBy { it }.fold(0) { acc, e -> acc + 1 }
            repeat(repeats) {
                stoneIteration = blinkOnce(stoneIteration)
            }
            return stoneIteration.values.sum()
        }
    }

    fun part1(input: String): Long {
        return StoneList(input).blinkAll(25)
    }


    fun part2(input: String): Long {
        return StoneList(input).blinkAll(75)
    }

    val testInput = readInputAsString("${DAY}_test")
    check(part1(testInput) == 55312L)

    val input = readInputAsString(DAY)
    check(part1(input) == 199753L)
    part1(input).println()
    part2(input).println()
}
