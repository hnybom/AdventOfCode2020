package fi.solita.advent.hnybom

import java.io.File

class Advent1 {

    private val input: List<Long> = File("/home/henriny/work/own/advent/src/main/resources/input.txt").readLines().map { it.toLong() }

    fun calculateAnswer() {
        input.forEachIndexed { index, l ->
            val filter = input.withIndex().filter { it.index != index }
            val added = filter.map { it.index to it.value + l }
            added.forEach {sec ->
                val filter2 = filter.filter { it.index != sec.first }
                val added2 = filter2.map { it.index to it.value + sec.second }
                val answers = added2.filter { it.second == 2020L }
                if (answers.isNotEmpty()) {
                    val second = input[answers.first().first]
                    val third = input[sec.first]
                    println("Match $l, $second and $third and the result is ${l * second * third}")
                }
            }

        }
    }
}

fun main() {
    Advent1().calculateAnswer()
}