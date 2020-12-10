package fi.solita.advent.hnybom

import java.io.File
import kotlin.math.min
import kotlin.math.pow

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
        val cache = mutableMapOf(0 to 1L)

        sortedJolts.forEach {
            cache[it] = (cache[it - 3] ?: 0L) + (cache[it - 2] ?: 0L) + (cache[it - 1] ?: 0L)
        }

        println("Permutations ${cache[sortedJolts.last()]}") //14173478093824

    }
}

fun main() {
    Advent10().calculateAnswer()
}