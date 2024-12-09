import com.github.shiguruikai.combinatoricskt.combinations

private const val DAY = "Day08"

private data class Antenna(
    val freq: Char,
    val x: Int,
    val y: Int
)

private data class Antidote(
    val x: Int,
    val y: Int
)

fun main() {
    class AntennaMap(input: List<String>) {
        val data = Map2d(input)
        val antennas = mutableListOf<Antenna>()

        init {
            data.forEachIndexed { y, x, item ->
                if (item != '.') {
                    antennas.add(Antenna(item, x, y))
                }
            }
        }

        private fun getAntidotesUp(a1: Antenna, a2: Antenna): Collection<Antidote> {
            val diffX = a1.x - a2.x
            val diffY = a2.y - a1.y

            val dotes = mutableSetOf<Antidote>()
            var currentX = a1.x
            var currentY = a1.y

            while (true) {
                val newX = currentX + diffX
                val newY = currentY - diffY
                if (data.isValidCoordinate(newX, newY)) {
                    dotes.add(Antidote(newX, newY))
                    currentX = newX
                    currentY = newY
                } else {
                    break
                }
            }

            return dotes
        }

        private fun getAntidotesDown(a1: Antenna, a2: Antenna): Collection<Antidote> {
            val diffX = a1.x - a2.x
            val diffY = a2.y - a1.y

            val dotes = mutableSetOf<Antidote>()
            var currentX = a2.x
            var currentY = a2.y

            while (true) {
                val newX = currentX - diffX
                val newY = currentY + diffY
                if (data.isValidCoordinate(newX, newY)) {
                    dotes.add(Antidote(newX, newY))
                    currentX = newX
                    currentY = newY
                } else {
                    break
                }
            }

            return dotes
        }

        private fun mapAntidotes(onlyFirst: Boolean): List<Antidote> = antennas
            .groupBy { it.freq }
            .filterValues { freq -> freq.size > 1 }
            .flatMap { (_, antennas) ->
                val dotes = mutableSetOf<Antidote>()
                antennas.combinations(2).forEach { (a1, a2) ->
                    val up = getAntidotesUp(a1, a2)
                    val down = getAntidotesDown(a1, a2)
                    when (onlyFirst) {
                        true -> {
                            up.firstOrNull()?.also { antidote -> dotes.add(antidote) }
                            down.firstOrNull()?.also { antidote -> dotes.add(antidote) }
                        }
                        false -> {
                            dotes.addAll(up)
                            dotes.addAll(down)
                            dotes.add(Antidote(a1.x, a1.y))
                            dotes.add(Antidote(a2.x, a2.y))
                        }
                    }
                }
                dotes
            }
            .distinct()

        fun countAntidotes(onlyFirst: Boolean): Int {
            val dotes = mapAntidotes(onlyFirst)
//            dotes.sortedBy { it.y }.forEach(::println)
            return dotes.size
        }
    }

    fun part1(input: List<String>): Int {
        return AntennaMap(input).countAntidotes(true)
    }


    fun part2(input: List<String>): Int {
        return AntennaMap(input).countAntidotes(false)
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
