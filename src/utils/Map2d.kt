package utils

data class MapItem<T>(val x: Int, val y: Int, val item: T)

sealed class Map2d<T>(val data: Array<Array<T>>) {
    class CharMap2d(input: List<String>) : Map2d<Char>(
        input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    )

    class IntMap2d(input: List<String>) : Map2d<Int>(
        input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
    )

    val ySize = data.size
    val xSize = data[0].size

    fun forEachIndexed(block: (y: Int, x: Int, item: T) -> Unit) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                block(y, x, data[y][x])
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

    fun elementUp(item: MapItem<Int>) = elementUp(item.x, item.y)
    fun elementUp(x: Int, y: Int) = when (y <= 0) {
        true -> null
        else -> MapItem(x, y - 1, data[y - 1][x])
    }

    fun elementDown(item: MapItem<Int>) = elementDown(item.x, item.y)
    fun elementDown(x: Int, y: Int) = when (y >= ySize - 1) {
        true -> null
        else -> MapItem(x, y + 1, data[y + 1][x])
    }

    fun elementLeft(item: MapItem<Int>) = elementLeft(item.x, item.y)
    fun elementLeft(x: Int, y: Int) = when (x <= 0) {
        true -> null
        else -> MapItem(x - 1, y, data[y][x - 1])
    }

    fun elementRight(item: MapItem<Int>) = elementRight(item.x, item.y)
    fun elementRight(x: Int, y: Int) = when (x >= xSize - 1) {
        true -> null
        else -> MapItem(x + 1, y, data[y][x + 1])
    }
}