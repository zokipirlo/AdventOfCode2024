private const val DAY = "Day11"

fun main() {
    fun part1(input: String): Int {
        var stones = input.split(" ").map { it.toLong() }
        repeat(25) {
            var newStones = mutableListOf<Long>()
            stones.forEach { stone ->
                when {
                    stone == 0L -> newStones.add(1L)
                    stone.toString().length % 2 == 0 -> {
                        val stoneString = stone.toString()
                        val (left, right) = stoneString.chunked(stoneString.length / 2)
                        newStones.add(left.toLong())
                        newStones.add(right.toLong())
                    }

                    else -> newStones.add(stone * 2024)
                }
            }
            stones = newStones
        }
        return stones.size
    }


    fun part2(input: String): Int {
        return 0
    }

    val testInput = readInputAsString("${DAY}_test")
    check(part1(testInput) == 55312)
    check(part2(testInput) == 0)

    val input = readInputAsString(DAY)
    part1(input).println()
    part2(input).println()
}
