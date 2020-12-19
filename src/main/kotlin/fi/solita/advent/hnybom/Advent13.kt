package fi.solita.advent.hnybom

import java.io.File

class Advent13 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input13.txt").readLines()

    
    fun calculateAnswer() {
        val departureTime = input.first().toInt()
        val lines = input[1].split(",").filter { it != "x" }.map { it.toInt() to it.toInt() }


        calculateAnswer1(departureTime to lines)
        calculateAnswer2(departureTime to lines)
    }

    private fun calculateAnswer1(data: Pair<Int, List<Pair<Int, Int>>>) {

        tailrec fun findFirstMatchOrContinue(departureTime: Int, times : List<Pair<Int, Int>>) : Pair<Int, Int> {
            val sortedTimes = times.filter { it.second >= departureTime }.sortedBy { it.second }
            return if(sortedTimes.size == times.size) sortedTimes.first()
            else findFirstMatchOrContinue(departureTime, times.map {
                    if(it.second < departureTime) it.first to it.second + it.first
                    else it
                }
            )
        }

        val firstMatch = findFirstMatchOrContinue(data.first, data.second)

        println("Find first line $firstMatch , answer is = ${(firstMatch.second - data.first) * firstMatch.first}")

    }


    private fun calculateAnswer2(data: Pair<Int, List<Pair<Int, Int>>>) {
    }
}

fun main() {
    Advent13().calculateAnswer()
}