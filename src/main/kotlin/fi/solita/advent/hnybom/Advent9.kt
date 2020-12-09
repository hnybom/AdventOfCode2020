package fi.solita.advent.hnybom

import java.io.File

class Advent9 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input9.txt").readLines().map { it.toLong() }

    
    fun calculateAnswer() {


        calculateAnswer1()
        calculateAnswer2()
    }

    private fun calculateAnswer1() {
        val previousAmount = 25

        input.drop(previousAmount).forEachIndexed { index, s ->
            val findSumrange = input.subList(index, index + previousAmount)
            val allSums = findSumrange.flatMapIndexed { index: Int, i: Long ->
                findSumrange.drop(index).map { it + i }
            }
            if (!allSums.contains(s)) {
                println("Didn't find pair for $s at $index")
            }

        }

    }


    private fun calculateAnswer2() {
        val weaknessNumber = 1398413738L
        input.forEachIndexed { index, orgNumber ->

            val accValues = input.drop(index).runningReduce { acc, l -> acc + l }
            if (accValues.contains(weaknessNumber)) {
                val indexOfWeakness = accValues.indexOf(weaknessNumber)
                val listOfSums = input.subList(index, indexOfWeakness + index + 1)
                val min = listOfSums.minOrNull() ?: 0
                val max = listOfSums.maxOrNull() ?: 0
                val answer =  min + max
                println("Found weakness sum from $index with $listOfSums and the answer is $answer")
                // 1367523572
            }
        }
    }
}

fun main() {
    Advent9().calculateAnswer()
}