import Direction.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.Point
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.measureTime

private const val DAY = "Day06"

private data class GuardPosition(
    val x: Int,
    val y: Int,
    val maxX: Int,
    val maxY: Int,
    val direction: Direction
) {
    val point by lazy { Point(x, y) }

    fun isInside(): Boolean = x > 0 && x < maxX && y > 0 && y < maxY

    fun makeMove() = when (direction) {
        UP -> copy(y = y - 1)
        DOWN -> copy(y = y + 1)
        LEFT -> copy(x = x - 1)
        RIGHT -> copy(x = x + 1)
    }

    fun changeDirection() = when (direction) {
        UP -> copy(direction = RIGHT)
        DOWN -> copy(direction = LEFT)
        LEFT -> copy(direction = UP)
        RIGHT -> copy(direction = DOWN)
    }
}

private enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun main() {

    class LabMap(input: List<String>, modifyX: Int? = null, modifyY: Int? = null) {
        val data = input.map { it.toCharArray() }.toTypedArray()
        val visited = mutableSetOf<GuardPosition>()
        var guardPosition: GuardPosition = run {
            data.forEachIndexed { y, line ->
                line.forEachIndexed { x, item ->
                    if (item == '^') {
                        return@run GuardPosition(x, y, line.lastIndex, data.lastIndex, UP)
                    }
                }
            }
            throw IllegalStateException("Guard position not found")
        }
        var steps = 0

        init {
            if (modifyX != null && modifyY != null) {
                val current = data[modifyY][modifyX]
                if (current == '#' || current == '^') {
                    throw IllegalStateException("Can't modify")
                } else {
                    data[modifyY][modifyX] = '#'
                }
            }
        }

        fun getNextPosition(): GuardPosition? {
            while (true) {
                val tryMove = guardPosition.makeMove()
                when {
                    data[tryMove.y][tryMove.x] == '#' -> {
                        guardPosition = guardPosition.changeDirection()
                    }

                    visited.contains(tryMove) -> {
                        return null
                    }

                    else -> {
                        visited.add(tryMove)
                        return tryMove
                    }
                }
            }
        }

        fun findExit(): Int {
//            println("$steps. Guard move: $guardPosition")
            while (guardPosition.isInside()) {
                guardPosition = getNextPosition() ?: return -1 // loop
                steps++
//                println("$steps. Guard move: $guardPosition")
            }
            return visited.distinctBy { position -> position.point }.size
        }

        fun findLoop(): Boolean {
            while (guardPosition.isInside()) {
                guardPosition = getNextPosition() ?: return true
            }

            return false
        }
    }

    fun part1(input: List<String>): Int {
        return LabMap(input).findExit()
    }


    fun part2(input: List<String>): Int {
        var loops = AtomicInteger(0)
        runBlocking(Dispatchers.Default) {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, item ->
                    launch {
                        val map = runCatching { LabMap(input, x, y) }.getOrNull()
                        val hasLoop = map?.findLoop() == true
                        if (hasLoop) {
                            loops.incrementAndGet()
                        }
                    }
                }
            }
        }
        return loops.get()
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput(DAY)
    part1(input).println()
    measureTime {
        part2(input).println()
    }.println()
}
