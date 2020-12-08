package fi.solita.advent.hnybom

import java.io.File

class Advent8 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input8.txt").readLines()

    
    fun calculateAnswer() {


        calculateAnswer1()
        calculateAnswer2()
    }

    private fun calculateAnswer1() {

        fun crawlCommandTree(commandMap: Map<Int, String>, index: Int, acc: Long) {
            val cmdStr = commandMap[index]
            if(cmdStr == null) {
                println("Duplicate command found. Last value $acc")
                return
            }
            val (cmd, value) = getCommands(cmdStr)
            val filteredCommandMap = commandMap.filter { it.key != index }
            when(cmd) {
                "acc" -> crawlCommandTree(filteredCommandMap, index + 1, acc + value)
                "jmp" -> crawlCommandTree(filteredCommandMap, index + value, acc)
                else -> crawlCommandTree(filteredCommandMap, index + 1, acc)
            }
        }

        val mapped = input.mapIndexed { index, s ->  index to s }.toMap()
        crawlCommandTree(mapped, 0, 0)

    }

    private fun getCommands(str : String): Pair<String, Int> {
        val split = str.split(" ")
        val cmd = split[0]
        val value = split[1].toInt()
        return Pair(cmd, value)
    }


    private fun calculateAnswer2() {
        val originalMaxIndex = input.size - 1
        fun crawlCommandTree(commandMap: Map<Int, String>, index: Int, acc: Long, isSwithced: Boolean = false) {
            val cmdStr = commandMap[index] ?: return
            if(index == originalMaxIndex) {
                println("Reached the end. Last value $acc and $cmdStr")
                return
            }

            val (cmd, value) = getCommands(cmdStr)
            val filteredCommandMap = commandMap.filter { it.key != index }
            when(cmd) {
                "acc" -> crawlCommandTree(filteredCommandMap, index + 1, acc + value, isSwithced)
                "jmp" -> crawlCommandTree(filteredCommandMap, index + value, acc, isSwithced)
                "nop" -> crawlCommandTree(filteredCommandMap, index + 1, acc, isSwithced)
            }
            if(!isSwithced) {
                when (cmd) {
                    "nop" -> crawlCommandTree(filteredCommandMap, index + value, acc, true)
                    "jmp" -> crawlCommandTree(filteredCommandMap, index + 1, acc, true)
                }
            }
        }

        val mapped = input.mapIndexed { index, s ->  index to s }.toMap()
        crawlCommandTree(mapped, 0, 0)
    }
}

fun main() {
    Advent8().calculateAnswer()
}