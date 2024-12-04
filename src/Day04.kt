private typealias Letters = Array<CharArray>

fun main() {
    var lastIndex = 0

    fun parseData(input: List<String>): Letters {
        val data = input.map { it.toCharArray() }.toTypedArray()
        lastIndex = data.size - 1
        return data
    }

    fun buildHorizontal(letters: Letters, x: Int, y: Int): String? {
        if (x + 3 > lastIndex) return null
        return letters[y].concatToString(x, x + 4)
    }

    fun buildVertical(letters: Letters, x: Int, y: Int): String? {
        if (y + 3 > lastIndex) return null
        return buildString {
            append(letters[y][x])
            append(letters[y + 1][x])
            append(letters[y + 2][x])
            append(letters[y + 3][x])
        }
    }

    fun buildDiagonalRight(letters: Letters, x: Int, y: Int): String? {
        if (y + 3 > lastIndex || x + 3 > lastIndex) return null
        return buildString {
            append(letters[y][x])
            append(letters[y + 1][x + 1])
            append(letters[y + 2][x + 2])
            append(letters[y + 3][x + 3])
        }
    }

    fun buildDiagonalLeft(letters: Letters, x: Int, y: Int): String? {
        if (y + 3 > lastIndex || x - 3 < 0) return null
        return buildString {
            append(letters[y][x])
            append(letters[y + 1][x - 1])
            append(letters[y + 2][x - 2])
            append(letters[y + 3][x - 3])
        }
    }

    fun checkXmas(word: String?): Boolean {
        return word == "XMAS" || word == "SAMX"
    }

    fun part1(input: List<String>): Int {
        var result = 0
        val letters = parseData(input)
        for (y in 0..lastIndex) {
            for (x in 0..lastIndex) {
                val w1 = buildHorizontal(letters, x, y)
                val w2 = buildVertical(letters, x, y)
                val w3 = buildDiagonalRight(letters, x, y)
                val w4 = buildDiagonalLeft(letters, x, y)
//                println("Words($x, $y): $w1 $w2 $w3 $w4")
                if (checkXmas(w1)) {
                    result++
                }
                if (checkXmas(w2)) {
                    result++
                }
                if (checkXmas(w3)) {
                    result++
                }
                if (checkXmas(w4)) {
                    result++
                }
            }
        }
//        println("Result $result")
        return result
    }

    fun buildXmas(letters: Letters, x: Int, y: Int): Pair<String, String>? {
        if (y + 2 > lastIndex || x + 2 > lastIndex) return null
        val diagRight = buildString {
            append(letters[y][x])
            append(letters[y + 1][x + 1])
            append(letters[y + 2][x + 2])
        }
        val diagLeft = buildString {
            append(letters[y][x + 2])
            append(letters[y + 1][x + 1])
            append(letters[y + 2][x])
        }
        return Pair(diagLeft, diagRight)
    }

    fun checkMas(word: String?): Boolean {
        return word == "MAS" || word == "SAM"
    }

    fun part2(input: List<String>): Int {
        var result = 0
        val letters = parseData(input)
        for (y in 0..lastIndex) {
            for (x in 0..lastIndex) {
                val xmas = buildXmas(letters, x, y)
//                println("Words($x, $y): $xmas")
                if (xmas != null && checkMas(xmas.first) && checkMas(xmas.second)) {
                    result++
                }
            }
        }
//        println("Result $result")
        return result
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
