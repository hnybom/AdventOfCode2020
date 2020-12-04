package fi.solita.advent.hnybom

import java.io.File


class Advent3 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input3.txt").readLines()

    fun calculateAnswer() {
        calculateAnswer1()
        calculateAnswer2()
    }

    private fun calculateAnswer1() {

        val rows = input.indices.map { it + 1 }.dropLast(1)

        val answer = rows.map {
            val row = input[it]
            val i = (it * 3) % row.length
            row[i] == '#'
        }.fold(0) { acc: Int, b: Boolean ->
            if (b) acc +1
            else acc
        }

        println("The first answer is $answer")

    }

    private fun calculateAnswer2() {
        val paths = listOf(
            1 to 1,
            3 to 1,
            5 to 1,
            7 to 1,
            1 to 2
        )

        val results = paths.map { rowToVisit ->
            val originalRowLength = input.size - 1
            val rowsToVisitTmp = input.indices.map { it + 1 }.filter {
                if (rowToVisit.second != 1) {
                    (it % rowToVisit.second) == 0
                } else true
            }
            val rowsToVisit = rowsToVisitTmp.dropLast(rowsToVisitTmp.last() - originalRowLength)

            rowsToVisit.mapIndexed { index, i -> {
                    val row = input[i]
                    val i = ((index + 1) * rowToVisit.first) % row.length
                    row[i] == '#'
                }
            }.map {
                it()
            }.fold(0L) { acc: Long, b: Boolean ->
                if (b) acc + 1
                else acc
            }
        }

        val ans = results.reduce{ acc, i -> acc * i}

        println("Second answer $ans")

    }
}

fun main() {
    Advent3().calculateAnswer()
}