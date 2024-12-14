import utils.println
import utils.readInput
import kotlin.math.absoluteValue

fun main() {
    fun isSameDirection(prevDif: Int, diff: Int): Boolean = when {
        prevDif == 0 -> true
        prevDif > 0 && diff > 0 -> true
        prevDif < 0 && diff < 0 -> true
        else -> false
    }

    fun checkReports(levels: List<Int>): Int {
        var prevDiff = 0
        levels.reduceIndexed { index, prev, current ->
            val diff = current - prev
            val absDiff = diff.absoluteValue

            if (absDiff == 0 || absDiff > 3 || !isSameDirection(prevDiff, diff)) {
                return index
            }

            prevDiff = diff
            current
        }

        return 0
    }


    fun isSafe(line: String): Boolean {
        val numbers = line.split(" ").map { it.toInt() }
        return checkReports(numbers) == 0
    }

    fun removeListItem(index: Int, list: List<Int>): List<Int> = list.toMutableList().apply { removeAt(index) }

    fun isSafe2(line: String): Boolean {
//        utils.println()

        val numbers = line.split(" ").map { it.toInt() }
        val index = checkReports(numbers)
        if (index == 0) {
//            utils.println("Safe in: $numbers")
            return true
        }

//        val part1 = removeListItem(index, numbers)
//        utils.println("Checking part1: $part1")
//        if (checkReports(part1) == 0) {
//            utils.println("Safe in: $part1")
//            return true
//        }
//        val part2 = removeListItem(index - 1, numbers)
//        utils.println("Checking part2: $part2")
//        if (checkReports(part2) == 0) {
//            utils.println("Safe in: $part2")
//            return true
//        }
//        utils.println("Unsafe in: $numbers")
//        return false
        return checkReports(removeListItem(index, numbers)) == 0 ||
                checkReports(removeListItem(index - 1, numbers)) == 0 ||
                checkReports(removeListItem(0, numbers)) == 0 // remove first to change direction check
    }

    fun part1(input: List<String>): Int {
        return input.count(::isSafe)
    }

    fun part2(input: List<String>): Int {
        return input.count(::isSafe2)
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
