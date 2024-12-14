package utils

class MapInt2d(val data: Array<IntArray>) {
    constructor(input: List<String>) : this(input.map { it.toCharArray().map(Char::digitToInt).toIntArray() }
        .toTypedArray())

    val ySize = data.size
    val xSize = data[0].size

    fun forEachIndexed(block: (y: Int, x: Int, item: Int) -> Unit) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                block(y, x, data[y][x])
            }
        }
    }

    fun forEach(block: (item: Int) -> Unit) {
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