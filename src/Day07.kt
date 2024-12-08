import com.github.shiguruikai.combinatoricskt.permutationsWithRepetition

private const val DAY = "Day07"


private class Equation(
    val result: Long,
    val numbers: List<Long>,
) {
    fun trySolve(operators: List<Char>): Boolean {
        val valid = operators.permutationsWithRepetition(numbers.size - 1).find { combination ->
            val calculate = numbers.reduceIndexed { index, tempResult, num ->
                if (tempResult > result) {
//                    println("too much $index, $tempResult, $num")
                    return@reduceIndexed tempResult
                }
                when (combination[index - 1]) {
                    '+' -> tempResult + num
                    '*' -> tempResult * num
                    '|' -> "${tempResult}${num}".toLong()
                    else -> throw UnsupportedOperationException("Unsupported operation ${combination[index - 1]}")
                }
            }
//            println("$combination -> $calculate | $result")
            calculate == result
        } != null
        return valid
    }

    companion object {
        fun parse(input: String): Equation {
            val (result, numbers) = input.split(":")
            return Equation(
                result = result.toLong(),
                numbers = numbers.trim().split(" ").map { string -> string.toLong() },
            )
        }
    }
}

fun main() {
    class Equations(input: List<String>) {
        private val equations = input.map(Equation::parse)

        fun getResult(operators: List<Char>): Long = equations
            .filter { value -> value.trySolve(operators) }
            .sumOf { value -> value.result }
    }

    fun part1(input: List<String>): Long {
        return Equations(input).getResult(listOf('+', '*'))
    }


    fun part2(input: List<String>): Long {
        return Equations(input).getResult(listOf('+', '*', '|'))
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
