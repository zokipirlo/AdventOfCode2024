import utils.Map2d
import utils.MapItem
import utils.println
import utils.readInput

private const val DAY = "Day10"

private class Trail(private val map: Map2d.IntMap2d, private val start: MapItem<Int>) {

    private val uniquePaths = mutableSetOf<MapItem<Int>>()
    private val ratings = mutableMapOf<MapItem<Int>, Int>()

    private fun findAllTrails(path: MutableList<MapItem<Int>>) {
        map.buildPath(path) {
            val lastItem = path.last()
            if (lastItem.item == 9) {
                uniquePaths.add(lastItem)
                ratings[lastItem] = ratings.getOrDefault(lastItem, 0) + 1
                return@buildPath emptyList()
            }

            val nextItem = lastItem.item + 1
            listOfNotNull(
                map.elementUp(lastItem),
                map.elementRight(lastItem),
                map.elementDown(lastItem),
                map.elementLeft(lastItem)
            ).filter { it.item == nextItem }
        }
    }

    fun getUniquePaths(): Int {
        findAllTrails(mutableListOf(start))
        return uniquePaths.size
    }

    fun getRatings(): Int {
        findAllTrails(mutableListOf(start))
        return ratings.values.sum()
    }
}

private class LavaIsland(input: List<String>) {
    private val map = Map2d.IntMap2d(input)

    fun getStartPoints(): List<MapItem<Int>> {
        val startPoints = mutableListOf<MapItem<Int>>()
        map.forEachIndexed { y, x, item ->
            if (item == 0) {
                startPoints.add(MapItem(x, y, 0))
            }
        }
        return startPoints
    }

    fun getUniquePaths() = getStartPoints().sumOf { Trail(map, it).getUniquePaths() }
    fun getRatings() = getStartPoints().sumOf { Trail(map, it).getRatings() }
}

fun main() {
    fun part1(input: List<String>): Int {
        val island = LavaIsland(input)
        return island.getUniquePaths()
    }


    fun part2(input: List<String>): Int {
        val island = LavaIsland(input)
        return island.getRatings()
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
