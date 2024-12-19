import utils.*

private const val DAY = "Day12"

private data class Plant(val x: Int, val y: Int, val plant: Char)
private sealed class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val item: Char) {

    fun getPlant() = when (this) {
        is Bottom -> item
        is Left -> item
        is Right -> item
        is Top -> item
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Line) return false

        if (x1 != other.x1) return false
        if (y1 != other.y1) return false
        if (x2 != other.x2) return false
        if (y2 != other.y2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x1
        result = 31 * result + y1
        result = 31 * result + x2
        result = 31 * result + y2
        return result
    }

    override fun toString(): String {
        return "Line(x1=$x1, y1=$y1, x2=$x2, y2=$y2, item=$item)"
    }


    class Left(item: MapItem<Char>) : Line(item.x, item.y, item.x, item.y + 1, item.item) {
        override fun toString(): String {
            return "Left.${super.toString()}"
        }
    }

    class Top(item: MapItem<Char>) : Line(item.x, item.y, item.x + 1, item.y, item.item) {
        override fun toString(): String {
            return "Top.${super.toString()}"
        }
    }

    class Right(item: MapItem<Char>) : Line(item.x + 1, item.y, item.x + 1, item.y + 1, item.item) {
        override fun toString(): String {
            return "Right.${super.toString()}"
        }
    }

    class Bottom(item: MapItem<Char>) : Line(item.x, item.y + 1, item.x + 1, item.y + 1, item.item) {
        override fun toString(): String {
            return "Bottom.${super.toString()}"
        }
    }
}

fun main() {
    class Garden(input: List<String>) {
        val map = Map2d.CharMap2d(input)
        val visited = Map2d.BooleanMap2d(input)
        val plants = map.mapIndexed { y, x, item -> Plant(x, y, item) }

        private fun getPaths() = plants.associateBy(
            { it },
            { plant ->
                val path = if (visited[plant.x, plant.y]) {
                    emptyList()
                } else {
                    val path = mutableListOf(MapItem<Char>(plant.x, plant.y, plant.plant))
                    visited[plant.x, plant.y] = true
                    map.buildPath(path) {
                        val lastItem = path.last()
                        listOfNotNull(
                            map.elementUp(lastItem),
                            map.elementRight(lastItem),
                            map.elementDown(lastItem),
                            map.elementLeft(lastItem)
                        ).filter { item ->
                            val isVisited = visited[item.x, item.y]
                            val valid = (item.item == lastItem.item && !isVisited)
                            if (valid) {
                                visited[item.x, item.y] = true
                            }
                            valid
                        }
                    }
                    path
                }
                path
            }
        ).filter { it.value.isNotEmpty() }

        fun getPerimeters() = getPaths().mapValues { (_, plants) ->
            val allLines = plants.flatMap { plant ->
                listOf(Line.Top(plant), Line.Right(plant), Line.Bottom(plant), Line.Left(plant))
            }
            val unique = allLines.groupBy { it }.filter { it.value.size == 1 }
            plants.size to unique.size
        }

        fun getSides() = getPaths().mapValues { (_, plants) ->
            val allLines = plants.flatMap { plant ->
                listOf(Line.Top(plant), Line.Right(plant), Line.Bottom(plant), Line.Left(plant))
            }
            val uniqueLines = allLines.groupBy { it }.filter { it.value.size == 1 }.keys

            // split horizontal and vertical lines
            val horizontal = uniqueLines.filter { it.y1 == it.y2 }
            val vertical = uniqueLines.filter { it.x1 == it.x2 }

            // group to get sides
            val groupHorizontalByY = horizontal.groupBy { it.y1 }
            val groupVerticalByX = vertical.groupBy { it.x1 }

            // can be gaps in horizontal line or vertical line
            val xCount = groupHorizontalByY.values.sumOf { lines ->
                val sorted = lines.sortedWith(compareBy({ it.x1 }, { it.x2 }, { it.y1 }, { it.y2 }))
                val ziped = sorted.zipWithNext { l1, l2 ->
                    when {
                        l1.x2 != l2.x1 -> 1
                        else -> when {
                            l1.getPlant() != l2.getPlant() -> 1
                            l1.javaClass != l2.javaClass -> 1 // change direction (inner - outer)
                            else -> 0
                        }
                    }
                }
                val sum = ziped.sum()
                sum + 1
            }.toLong()
            val yCount = groupVerticalByX.values.sumOf { lines ->
                val sorted = lines.sortedWith(compareBy({ it.y1 }, { it.y2 }, { it.x1 }, { it.x2 }))
                val ziped = sorted.zipWithNext { l1, l2 ->
                    when {
                        l1.y2 != l2.y1 -> 1
                        else -> when {
                            l1.getPlant() != l2.getPlant() -> 1
                            l1.javaClass != l2.javaClass -> 1 // change direction (inner - outer)
                            else -> 0
                        }
                    }
                }
                val sum = ziped.sum()
                sum + 1
            }.toLong()
            plants.size to (xCount + yCount)
        }
    }

    fun part1(input: List<String>): Int {
        val garden = Garden(input)
        val perimeters = garden.getPerimeters()
        return perimeters.values.sumOf { (count, lines) -> count * lines }
    }

    fun part2(input: List<String>): Long {
        val garden = Garden(input)
        val sides = garden.getSides()
        return sides.values.sumOf { (count, lines) -> count * lines }
    }

    val testInput = readInput("${DAY}_test")
    val test1 = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent()
    val test2 = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
""".trimIndent()
    val test3 = """
        EEEEE
        EXXXX
        EEEEE
        EXXXX
        EEEEE
    """.trimIndent()
    val test4 = """
        AAAAAA
        AAABBA
        AAABBA
        ABBAAA
        ABBAAA
        AAAAAA
    """.trimIndent()

    check(part1(readStringAsLines(test1)) == 140)
    check(part1(readStringAsLines(test2)) == 772)
    check(part1(testInput) == 1930)
//
    check(part2(readStringAsLines(test1)) == 80L)
    check(part2(readStringAsLines(test2)) == 436L)
    check(part2(readStringAsLines(test3)) == 236L)
    check(part2(readStringAsLines(test4)) == 368L)
    check(part2(testInput) == 1206L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
