package fi.solita.advent.hnybom

import java.io.File

data class Passport(val lines : List<String>) {

    fun asString() = lines.joinToString(" ")

}

class Advent4 {

    private val emptyLineRegex = "^\\s*\$".toRegex()


    private val requiredFields = mapOf(
            "byr" to true,
            "iyr" to true,
            "eyr" to true,
            "hgt" to true,
            "hcl" to true,
            "ecl" to true,
            "pid" to true,
            "cid" to false,

    )

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input4.txt").readLines()


    fun calculateAnswer() {


        val emptyLineIndexes = input.mapIndexed { index, s ->
            emptyLineRegex.matches(s) to index
        }.filter { it.first }.map { it.second }


        fun splitToPassport(emptyIndexes : List<Int>, lastIndex: Int): List<Passport> {
            if(emptyIndexes.isEmpty()) return listOf()
            val toIndex = emptyIndexes.first()
            val passportLines = input.subList(lastIndex, toIndex)
            val nextIndexes = emptyIndexes.drop(1)
            return listOf(Passport(passportLines)) + splitToPassport(nextIndexes, toIndex + 1)

        }

        val passports = splitToPassport(emptyLineIndexes, 0)

        calculateAnswer1(passports)
        calculateAnswer2(passports)
    }

    private fun calculateAnswer1(passports: List<Passport>) {
        val validPassports = passports.filter { p ->
            val s = p.asString()
            val requiredFound = requiredFields.map {
                if(it.value) s.contains(it.key)
                else true
            }

            !requiredFound.contains(false)
        }

        println("Valid passport count ${validPassports.size}")

    }

    private fun calculateAnswer2(passports: List<Passport>) {


    }
}

fun main() {
    Advent4().calculateAnswer()
}