package fi.solita.advent.hnybom

import java.io.File

class Advent13 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input13.txt").readLines()

    
    fun calculateAnswer() {

        calculateAnswer1()
        calculateAnswer2()
    }

    private fun calculateAnswer1() {

        tailrec fun findFirstMatchOrContinue(departureTime: Int, times : List<Pair<Int, Int>>) : Pair<Int, Int> {
            val sortedTimes = times.filter { it.second >= departureTime }.sortedBy { it.second }
            return if(sortedTimes.size == times.size) sortedTimes.first()
            else findFirstMatchOrContinue(departureTime, times.map {
                    if(it.second < departureTime) it.first to it.second + it.first
                    else it
                }
            )
        }

        val departureTime = input.first().toInt()
        val lines = input[1].split(",").filter { it != "x" }.map { it.toInt() to it.toInt() }

        val firstMatch = findFirstMatchOrContinue(departureTime, lines)

        println("Find first line $firstMatch , answer is = ${(firstMatch.second - departureTime) * firstMatch.first}")

    }


    private fun calculateAnswer2() {

        val originalLines = input[1].split(",")
                .mapIndexed { index, s ->  index to s }
                .filter { it.second != "x" }
                .map { it.first to it.second.toLong() }
                //.sortedBy { it.second }

        val startTime = originalLines.first().second

        tailrec fun findTrailingDepartureTimes(lines: List<Pair<Int, Long>>, time:Long, inc: Long): Long {

            tailrec fun incrementUntil(v: Long, time: Long, inc: Long, index: Int) : Long {
                return if(((time + index) % v) != 0L) incrementUntil(v, time + inc, inc, index)
                else time
            }

            val currentTime = lines.first()
            val newTime = incrementUntil(currentTime.second, time, inc, currentTime.first)
            val remaining = lines.drop(1)
            return if(remaining.isEmpty()) newTime
            else findTrailingDepartureTimes(remaining, newTime, inc * currentTime.second)

        }

        val solution = findTrailingDepartureTimes(originalLines.drop(1), startTime, startTime)

        println("Solution for second is $solution")
    }
}

fun main() {
    Advent13().calculateAnswer()
}