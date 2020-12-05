package fi.solita.advent.hnybom

import java.io.File

data class Boardingpass(val str: String, val row: Int, val column : Int, val id: Int)

class Advent5 {

    private val input = File("/home/henriny/work/AdventOfCode2020/src/main/resources/input5.txt").readLines()
    private val rows = (0..127).toList()
    private val columns = (0..7).toList()
    
    fun calculateAnswer() {

        val passes = input.map {
            val rowStr = it.subSequence(0, 7)
            val columnStr = it.subSequence(7, 10)

            val passRow = rowStr.fold(rows) { acc, c ->
                if(c == 'F') {
                    acc.subList(0, acc.size / 2)
                } else {
                    acc.subList(acc.size / 2, acc.size)
                }
            }

            val passColumn = columnStr.fold(columns) { acc, c ->
                if(c == 'L') {
                    acc.subList(0, acc.size / 2)
                } else {
                    acc.subList(acc.size / 2, acc.size)
                }
            }

            Boardingpass(
                    it,
                    passRow.first(),
                    passColumn.first(),
                    passRow.first() * 8 + passColumn.first()
            )
        }

        calculateAnswer1(passes)
        calculateAnswer2(passes)
    }

    private fun calculateAnswer1(passes : List<Boardingpass>) {

        val highestId = passes.maxByOrNull { it.id }
        println("Highest pass id is ${highestId?.id}")

    }


    private fun calculateAnswer2(passes : List<Boardingpass>) {

        val sorted = passes.sortedBy { it.id }
        val seatNextToMissing = sorted.filterIndexed { index, boardingpass ->
            when (index) {
                0 -> false
                sorted.size -1 -> false
                else -> {
                    sorted[index -1].id + 1 != boardingpass.id
                }
            }
        }

        println("Seat ${seatNextToMissing.first().id} is missing previous seat so the answer is ${seatNextToMissing.first().id -1}")
    }
}

fun main() {
    Advent5().calculateAnswer()
}