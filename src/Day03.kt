private sealed interface Command {
    data class Mul(val a: Int, val b: Int): Command {
        val result = a * b
    }
    data object Do : Command
    data object Dont : Command
}

fun main() {

    fun findMatches(input: String): List<Command> {
        val regex = Regex(pattern = "(mul\\([0-9]{1,3},[0-9]{1,3}\\)|do\\(\\)|don't\\(\\))")
        val rawCommands = regex.findAll(input).map { it.value}.toList()
        return rawCommands.mapNotNull {
            when {
                it.startsWith("mul") -> {
                    val mulCommand = it.removePrefix("mul(").removeSuffix(")").split(",")
                    Command.Mul(mulCommand[0].toInt(), mulCommand[1].toInt())
                }
                it.startsWith("don't") -> {
                    Command.Dont
                }
                it.startsWith("do") -> {
                    Command.Do
                }
                else -> null
            }
        }
    }

    fun calculate(line: String): Int {
        return findMatches(line).filterIsInstance<Command.Mul>().sumOf { it.result }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf(::calculate)
    }

    var isDo = true
    fun calculate2(line: String): Int {
        val commands = findMatches(line)
//        println("Commands: $commands")
        val muls = commands.filter { command ->
            when (command) {
                Command.Do -> {
                    isDo = true
                    false
                }
                Command.Dont -> {
                    isDo =  false
                    false
                }
                is Command.Mul -> {
                    isDo
                }
            }
        }
//        println("Muls: $muls")
        return muls.sumOf { (it as Command.Mul).result }
    }

    fun part2(input: List<String>): Int {
        // 101629183 too high
        return input.sumOf(::calculate2)
    }

    check(part1(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}