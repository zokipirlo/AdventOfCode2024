package utils

data class MapItem<T>(val x: Int, val y: Int, val item: T)

sealed class Map2d<T>(val data: Array<Array<T>>) {
    class CharMap2d(input: List<String>) : Map2d<Char>(
        input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    )

    class IntMap2d(input: List<String>) : Map2d<Int>(
        input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
    )

    class BooleanMap2d(input: List<String>) : Map2d<Boolean>(
        input.map { it.toCharArray().map { false }.toTypedArray() }.toTypedArray()
    )

    val ySize = data.size
    val xSize = data[0].size

    operator fun get(x: Int, y: Int) = data[y][x]

    fun getOrNull(x: Int, y: Int) = when (x < xSize &&  y < ySize && x >=0 && y >= 0) {
        true -> data[y][x]
        false -> null
    }

    operator fun set(x: Int, y: Int, value: T) {
        data[y][x] = value
    }

    fun forEachIndexed(block: (y: Int, x: Int, item: T) -> Unit) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                block(y, x, data[y][x])
            }
        }
    }

    inline fun <R> mapValues(transform: (y: Int, x: Int, item: T) -> T) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                data[y][x] = transform(y, x, data[y][x])
            }
        }
    }

    inline fun <R> mapIndexed(transform: (y: Int, x: Int, item: T) -> R): List<R> {
        val items = mutableListOf<R>()
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                items.add(transform(y, x, data[y][x]))
            }
        }
        return items
    }

    fun forEach(block: (item: T) -> Unit) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                block(data[y][x])
            }
        }
    }

    fun isValidCoordinate(x: Int, y: Int) = x in 0..<xSize && y in 0..<ySize

    fun elementUp(item: MapItem<T>) = elementUp(item.x, item.y)
    fun elementUp(x: Int, y: Int) = when (y <= 0) {
        true -> null
        else -> MapItem(x, y - 1, data[y - 1][x])
    }

    fun elementDown(item: MapItem<T>) = elementDown(item.x, item.y)
    fun elementDown(x: Int, y: Int) = when (y >= ySize - 1) {
        true -> null
        else -> MapItem(x, y + 1, data[y + 1][x])
    }

    fun elementLeft(item: MapItem<T>) = elementLeft(item.x, item.y)
    fun elementLeft(x: Int, y: Int) = when (x <= 0) {
        true -> null
        else -> MapItem(x - 1, y, data[y][x - 1])
    }

    fun elementRight(item: MapItem<T>) = elementRight(item.x, item.y)
    fun elementRight(x: Int, y: Int) = when (x >= xSize - 1) {
        true -> null
        else -> MapItem(x + 1, y, data[y][x + 1])
    }

    fun buildPath(path: MutableList<MapItem<T>>, nextCandidates: () -> List<MapItem<T>>) {
        nextCandidates().forEach { candidate ->
            buildPath(path.also { it.add(candidate) }, nextCandidates)
        }
    }
}