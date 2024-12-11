private const val DAY = "Day10"

private data class MapPath(
    val start: MapItem<Int>,
    val end: MapItem<Int>,
)

private class LavaIsland(input: List<String>) {
    private val map = MapInt2d(input)
    private val ratings = mutableMapOf<MapPath, Int>()
    private val uniquePaths = mutableSetOf<MapPath>()

    fun moveFrom(num: Int, x: Int, y: Int, path: List<MapItem<Int>>) {
        if (num == 9) {
            val uniquePath = MapPath(path.first(), path.last())
            uniquePaths.add(uniquePath)
            ratings[uniquePath] = ratings.getOrDefault(uniquePath, 0) + 1
//            println("Found path: $path")
            return
        }

        val nextItem = num + 1
        val candidates = listOfNotNull(
            map.elementUp(x, y),
            map.elementRight(x, y),
            map.elementDown(x, y),
            map.elementLeft(x, y)
        ).filter { it.item == nextItem }

        candidates.forEach { candidate ->
            moveFrom(candidate.item, candidate.x, candidate.y, path.toMutableList().also { it.add(candidate) })
        }
    }

    fun mapTrails() {
        map.forEachIndexed { y, x, item ->
            if (item == 0) {
                moveFrom(0, x, y, listOf(MapItem(x, y, 0)))
            }
        }
    }

    fun getUniquePaths() = uniquePaths.size
    fun getRatings() = ratings.values
}

fun main() {
    fun part1(input: List<String>): Int {
        val island = LavaIsland(input)
        island.mapTrails()
        return island.getUniquePaths()
    }


    fun part2(input: List<String>): Int {
        val island = LavaIsland(input)
        island.mapTrails()
        return island.getRatings().sum()
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
