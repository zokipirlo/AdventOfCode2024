import com.github.shiguruikai.combinatoricskt.permutationsWithRepetition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import utils.concat
import utils.println
import utils.readInput
import kotlin.time.measureTime

private const val DAY = "Day07"


private class Equation(
    val result: Long,
    val numbers: List<Long>,
) {
    fun trySolve(operators: List<Char>): Long? {
        val valid = operators.permutationsWithRepetition(numbers.size - 1).find { combination ->
            val calculate = numbers.reduceIndexed { index, tempResult, num ->
                if (tempResult > result) {
//                    utils.println("too much $index, $tempResult, $num")
                    return@reduceIndexed tempResult
                }
                when (combination[index - 1]) {
                    '+' -> tempResult + num
                    '*' -> tempResult * num
                    '|' -> tempResult.concat(num)
                    else -> throw UnsupportedOperationException("Unsupported operation ${combination[index - 1]}")
                }
            }
//            utils.println("$combination -> $calculate | $result")
            calculate == result
        } != null
        return if (valid) result else null
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

        suspend fun getResult(operators: List<Char>): Long = coroutineScope {
            equations
                .map { value -> async { value.trySolve(operators) } }
                .sumOf { deferred -> deferred.await() ?: 0L }
        }
    }

    fun part1(input: List<String>): Long {
        return runBlocking(Dispatchers.Default) {
            Equations(input).getResult(listOf('+', '*'))
        }
    }


    fun part2(input: List<String>): Long {
        return runBlocking(Dispatchers.Default) {
            Equations(input).getResult(listOf('+', '*', '|'))
        }
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput(DAY)
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}
