package fi.solita.advent.hnybom

import java.io.File

class Advent10 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input10.txt").readLines().map { it.toInt() }

    
    fun calculateAnswer() {


        calculateAnswer1()
        calculateAnswer2()
    }

    private fun calculateAnswer1() {
        val sortedInput = input.sorted()
        val sortedJolts = listOf(0) + sortedInput + listOf(sortedInput.last() +3)
        val threeGap = sortedJolts.drop(1).filterIndexed { index, i ->  i - sortedJolts[index] == 3}
        val oneGap = sortedJolts.drop(1).filterIndexed { index, i ->  i - sortedJolts[index] == 1}
        println("One gap ${oneGap.size}, three gap ${threeGap.size}. Multiplied ${oneGap.size * threeGap.size}")
    }


    private fun calculateAnswer2() {
        val sortedInput = input.sorted()
        val sortedJolts = sortedInput + listOf(sortedInput.last() + 3)

        fun traversePaths(paths : Map<Int, Long>, adapters: List<Int>) : Long {
            if(adapters.isEmpty()) return paths[sortedInput.last()] ?: 0
            val adapter = adapters.first()
            val adapterPaths = (paths[adapter -3]  ?: 0L) + (paths[adapter - 2]  ?: 0L) + (paths[adapter - 1]  ?: 0L)
            val adapterVal = adapter to adapterPaths
            return traversePaths(paths + adapterVal, adapters.drop(1))
        }

        val result = traversePaths(mapOf(0 to 1L), sortedJolts)

        println("Permutations $result") //14173478093824

    }
}

fun main() {
    Advent10().calculateAnswer()
}