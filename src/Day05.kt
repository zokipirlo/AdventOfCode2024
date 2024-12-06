private const val DAY = "Day05"

fun main() {
    class OrderRule(
        val before: MutableList<Int> = mutableListOf(),
        val after: MutableList<Int> = mutableListOf()
    )

    class PrintQueue {
        val instructions = mutableListOf<List<Int>>()
        val orderMap = mutableMapOf<Int, OrderRule>()

        fun parseRule(rule: String) {
            val (before, after) = rule.split("|").map { string -> string.toInt() }
            orderMap.getOrPut(before) { OrderRule() }.also { rule ->
                rule.after.add(after)
            }
            orderMap.getOrPut(after) { OrderRule() }.also { rule ->
                rule.before.add(before)
            }
        }

        fun parseInstruction(string: String) {
            instructions.add(string.split(",").map { it.toInt() })
        }

        fun parseData(input: List<String>) {
            var parseRules = true
            input.forEach { line ->
                if (line.isEmpty()) {
                    parseRules = false
                    return@forEach
                }
                when (parseRules) {
                    true -> parseRule(line)
                    false -> parseInstruction(line)
                }
            }
        }

        fun validateInstructionItem(instruction: List<Int>, index: Int): Int {
            val num = instruction[index]

            val rules = orderMap[num]
            if (rules == null) {
                return -1
            }

            val beforeNums = instruction.subList(0, index)
            val afterNums = instruction.subList(index + 1, instruction.size)

            val errorBefore = beforeNums.find { before -> rules.after.contains(before) }
            if (errorBefore != null) {
//            println("error rule $num|$errorBefore")
                return instruction.indexOf(errorBefore)
            }

            val errorAfter = afterNums.find { after -> rules.before.contains(after) }
            if (errorAfter != null) {
//            println("error rule errorAfter|$num")
                return instruction.indexOf(errorAfter)
            }

            return -1
        }

        fun validateInstruction(instruction: List<Int>): Boolean {
            return instruction.indices.all { index ->
                validateInstructionItem(instruction, index) == -1
            }
        }

        fun fixInstruction(instruction: List<Int>): List<Int> {
            val results = instruction.indices.map { index ->
                index to validateInstructionItem(instruction, index)
            }
            val firstError = results.firstOrNull { pair -> pair.second != -1 }

            if (firstError != null) {
                val item = instruction[firstError.second]
                val fixed = instruction.toMutableList().apply {
                    removeAt(firstError.second)
                    add(firstError.first, item)
                }
                return fixInstruction(fixed)
            } else {
                return instruction
            }
        }
    }

    fun part1(input: List<String>): Int {
        val printQueue = PrintQueue()
        printQueue.parseData(input)
        return printQueue.instructions.filter {
            printQueue.validateInstruction(it)
        }.sumOf { valid ->
            valid[valid.size / 2]
        }
    }


    fun part2(input: List<String>): Int {
        val printQueue = PrintQueue()
        printQueue.parseData(input)
        return printQueue.instructions.filterNot {
            printQueue.validateInstruction(it)
        }.map { problematic ->
            printQueue.fixInstruction(problematic)
        }.sumOf { valid ->
            valid[valid.size / 2]
        }
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
