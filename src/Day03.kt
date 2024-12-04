private sealed interface Command {
    data class Mul(val a: Int, val b: Int): Command {
        fun result() = a * b
    }
    data object Do : Command
    data object Dont : Command
}

fun main() {
    fun findMatches(input: String): Sequence<Command> {
        // test with https://rubular.com/
        val regex = Regex(pattern = "(mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|(do)\\(\\)|(don't)\\(\\))")
        return regex.findAll(input).mapNotNull { match ->
            val (fullCommand, mula, mulb, isDo, isDont) = match.destructured
            when {
                isDo.isNotEmpty() -> Command.Do
                isDont.isNotEmpty() -> Command.Dont
                fullCommand.startsWith("mul") -> Command.Mul(mula.toInt(), mulb.toInt())
                else -> null
            }
        }
    }

    fun part1(input: String): Int {
        return findMatches(input)
            .filterIsInstance<Command.Mul>()
            .sumOf { it.result() }
    }

    fun part2(input: String): Int {
        val commands = findMatches(input)
        var isDo = true
        return commands.sumOf { command ->
            when (command) {
                Command.Do -> {
                    isDo = true
                    0
                }
                Command.Dont -> {
                    isDo =  false
                    0
                }
                is Command.Mul if isDo -> command.result()
                is Command.Mul -> 0
            }
        }
    }

    check(part1("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))") == 161)
    check(part2("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") == 48)

    val input = readInputAsString("Day03")
    part1(input).println()
    part2(input).println()
}
