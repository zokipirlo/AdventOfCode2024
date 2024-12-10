private const val DAY = "Day09"

private class DiskMap(input: String) {
    val blocks = mutableListOf<Int>()
    var fileId = 0
    var emptySpaces = 0
    val fileIdSize = mutableMapOf<Int, Int>()
    val fileStartIndices = mutableMapOf<Int, Int>()

    init {
        input.forEachIndexed { index, ch ->
            val size = ch.digitToInt()
            when (index % 2 == 0) {
                true -> {
                    fileIdSize[fileId] = size
                    fileStartIndices[fileId] = blocks.size
                    repeat(size) { blocks.add(fileId) }
                    fileId++
                }

                false -> {
                    repeat(size) { blocks.add(-1) }
                    emptySpaces += size
                }
            }
        }
    }

    fun moveBlocks() {
        var freeSpaceIndex = -1
        var lastFileIndex = blocks.size
        while (true) {
            freeSpaceIndex = blocks.withIndex().first { it.index > freeSpaceIndex && it.value == -1 }.index
            val (fileIndex, fileIdNum) = blocks.withIndex().last { it.index < lastFileIndex && it.value != -1 }
            lastFileIndex = fileIndex
            if (freeSpaceIndex > lastFileIndex) {
                break
            }
            moveBlock(freeSpaceIndex, lastFileIndex, fileIdNum)
        }
    }

    private fun moveBlock(toIndex: Int, fromIndex: Int, fileId: Int) {
        blocks[toIndex] = fileId
        blocks[fromIndex] = -1
    }

    fun fileEmptySpaceIndex(maxIndex: Int, size: Int): Int {
        var countEmptyBlocks = 0
        (0..maxIndex).forEach { index ->
            when (blocks[index]) {
                -1 -> {
                    countEmptyBlocks++
                    if (countEmptyBlocks == size) {
                        return index
                    }
                }
                else -> {
                    countEmptyBlocks = 0
                }
            }
        }
        return -1
    }

    fun moveFiles() {
        var lastFileId = fileId - 1
        while (lastFileId >= 0) {
            val fileSize = fileIdSize[lastFileId]!!
            val fileStartIndex = fileStartIndices[lastFileId]!!
            val moveEndIndex = fileEmptySpaceIndex(fileStartIndex, fileSize)
            if (moveEndIndex != -1) {
                repeat(fileSize) { moveIndex ->
                    moveBlock(moveEndIndex - moveIndex, fileStartIndex + moveIndex, lastFileId)
                }
            }
            lastFileId--
        }
    }

    fun calculate(): Long {
        return blocks.withIndex().sumOf { item ->
            when (item.value) {
                -1 -> 0L
                else -> item.index * item.value.toLong()
            }
        }
    }
}

fun main() {

    fun part1(input: String): Long {
        val diskMap = DiskMap(input)
        diskMap.moveBlocks()
        return diskMap.calculate()
    }


    fun part2(input: String): Long {
        val diskMap = DiskMap(input)
//        println(diskMap.blocks)
        diskMap.moveFiles()
//        println(diskMap.blocks)
        return diskMap.calculate()
    }

//    val testInput = readInput("${DAY}_test")
    val testInput = "2333133121414131402"
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInputAsString(DAY)
    part1(input).println()
    part2(input).println()
}
