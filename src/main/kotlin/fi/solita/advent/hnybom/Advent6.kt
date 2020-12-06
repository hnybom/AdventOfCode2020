package fi.solita.advent.hnybom

import java.io.File


data class GroupsAnswers(val answers: List<String>) {

    fun getUniqueAnswers(): Set<Char> {
        return answers.flatMap { it.toList() }.toSet()
    }

    fun getCommonAnswers(): Map<Char, List<Char>> {
        val peopleCount = answers.size
        return answers.flatMap { it.toList() }.groupBy { it }.filter {
            it.value.size == peopleCount
        }
    }

}
class Advent6 {

    private val emptyLineRegex = "^\\s*\$".toRegex()
    private val input = File("/home/henriny/work/AdventOfCode2020/src/main/resources/input6.txt").readLines()

    fun calculateAnswer() {

        val emptyLineIndexes = input.mapIndexed { index, s ->
            emptyLineRegex.matches(s) to index
        }.filter { it.first }.map { it.second }


        fun splitToAnswers(emptyIndexes : List<Int>, lastIndex: Int): List<GroupsAnswers> {
            if(emptyIndexes.isEmpty()) return listOf(GroupsAnswers(input.subList(lastIndex, input.size)))
            val toIndex = emptyIndexes.first()
            val passportLines = input.subList(lastIndex, toIndex)
            val nextIndexes = emptyIndexes.drop(1)
            return listOf(GroupsAnswers(passportLines)) + splitToAnswers(nextIndexes, toIndex + 1)

        }

        val answers = splitToAnswers(emptyLineIndexes, 0)

        calculateAnswer1(answers)
        calculateAnswer2(answers)
    }

    private fun calculateAnswer1(answers: List<GroupsAnswers>) {
        val total = answers.map { it.getUniqueAnswers().size }.reduce{ acc, i -> acc + i }
        println("Total count is $total")
    }


    private fun calculateAnswer2(answers: List<GroupsAnswers>) {
        val totalCommon = answers.map { it.getCommonAnswers().size }.reduce{ acc, i -> acc + i }
        println("Total count for common is $totalCommon")
    }
}

fun main() {
    Advent6().calculateAnswer()
}