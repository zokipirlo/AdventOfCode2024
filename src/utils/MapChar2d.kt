package utils

class MapChar2d(val data: Array<CharArray>) {
    constructor(input: List<String>) : this(input.map { it.toCharArray() }.toTypedArray())

    val ySize = data.size
    val xSize = data[0].size

    fun forEachIndexed(block: (y: Int, x: Int, item: Char) -> Unit) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                block(y, x, data[y][x])
            }
        }
    }

    inline fun <T> mapIndexed(transform: (y: Int, x: Int, item: Char) -> T): List<T> {
        val items = mutableListOf<T>()
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                items.add(transform(y, x, data[y][x]))
            }
        }
        return items
    }

    fun forEach(block: (item: Char) -> Unit) {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                block(data[y][x])
            }
        }
    }

    fun isValidCoordinate(x: Int, y: Int) = x in 0..<xSize && y in 0..<ySize
}