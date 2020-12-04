package fi.solita.advent.hnybom

import java.io.File

data class RuleString(val min: Int, val max: Int, val char: Char, val content: String)

class Advent2 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input2.txt").readLines().map {
        val ruleset = it.split(":", "-")
        val maxAndChar = ruleset[1].split(" ")
        RuleString(ruleset[0].toInt(), maxAndChar[0].toInt(), maxAndChar[1][0], ruleset[2].trim())

    }

    fun calculateAnswer() {
        val validPasswords = input.filter { rule ->
            val count = rule.content.count { it == rule.char }
            count >= rule.min && count <= rule.max
        }

        println("Valid password count ${validPasswords.size}")
    }

    fun calculateAnswer2() {
        val validPasswords = input.filter { rule ->

            val minChar = rule.content[rule.min - 1]
            val maxChar = rule.content[rule.max - 1]

            val minIsHit = minChar == rule.char
            val maxIsHit = maxChar == rule.char

            if(minIsHit) !maxIsHit
            else maxIsHit

        }

        println("Valid password count for step 2 is ${validPasswords.size}")
    }
}

fun main() {
    Advent2().calculateAnswer()
    Advent2().calculateAnswer2()
}