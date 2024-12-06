private const val DAY = "Day06"

private data class GuardPosition(
    val x: Int,
    val y: Int,
    val maxX: Int,
    val maxY: Int
) {
    fun isInside(): Boolean = x > 0 && x < maxX && y > 0 && y < maxY
}

private enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun makeMove(position: GuardPosition) = when (this) {
        UP -> position.copy(y = position.y - 1)
        DOWN -> position.copy(y = position.y + 1)
        LEFT -> position.copy(x = position.x - 1)
        RIGHT -> position.copy(x = position.x + 1)
    }

    fun changeDirection() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }
}

fun main() {

    class LabMap(input: List<String>) {
        val data = input.map { it.toCharArray() }.toTypedArray()
        val visited = mutableSetOf<GuardPosition>()

        var guardPosition: GuardPosition = run {
            data.forEachIndexed { y, line ->
                line.forEachIndexed { x, item ->
                    if (item == '^') {
                        return@run GuardPosition(x, y, line.lastIndex, data.lastIndex)
                    }
                }
            }
            throw IllegalStateException("Guard position not found")
        }
        var steps = 0
        var direction = Direction.UP

        fun getNextPosition(): GuardPosition {
            while (true) {
                val tryMove = direction.makeMove(guardPosition)
                if (data[tryMove.y][tryMove.x] == '#') {
                    direction = direction.changeDirection()
                } else {
                    return tryMove
                }
            }
        }

        fun findExit(): Int {
//            println("$steps. Guard move: $guardPosition")
            while (guardPosition.isInside()) {
                guardPosition = getNextPosition()
                visited.add(guardPosition)
                steps++
//                println("$steps. Guard move: $guardPosition")
            }
            return visited.size
        }
    }

    fun part1(input: List<String>): Int {
        return LabMap(input).findExit()
    }


    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 41)
//    check(part2(testInput) == 0)

    val input = readInput(DAY)
    part1(input).println()
//    part2(input).println()
}
