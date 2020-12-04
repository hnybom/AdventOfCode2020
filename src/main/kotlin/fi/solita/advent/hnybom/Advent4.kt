package fi.solita.advent.hnybom

import java.io.File

data class Passport(val lines : List<String>) {

    fun asString() = lines.joinToString(" ")

    fun parameterMap() : Map<String, String> {
        val fields = asString().split(" ")
        return fields.map {
            val split = it.split(":")
            split[0] to split[1]
        }.toMap()
    }

}

class Advent4 {

    private val emptyLineRegex = "^\\s*\$".toRegex()

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input4.txt").readLines()


    fun calculateAnswer() {


        val emptyLineIndexes = input.mapIndexed { index, s ->
            emptyLineRegex.matches(s) to index
        }.filter { it.first }.map { it.second }


        fun splitToPassport(emptyIndexes : List<Int>, lastIndex: Int): List<Passport> {
            if(emptyIndexes.isEmpty()) return listOf(Passport(input.subList(lastIndex, input.size)))
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
        val requiredFields = mapOf(
                "byr" to true,
                "iyr" to true,
                "eyr" to true,
                "hgt" to true,
                "hcl" to true,
                "ecl" to true,
                "pid" to true,
                "cid" to false,

                )

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

        fun generateYearRangeValidator(start: Int, stop: Int) : (String?) -> Boolean {
            return {
                if (it == null) {
                    false
                } else {
                    try {
                        val year = it.toInt()
                        year in start..stop
                    } catch (e: NumberFormatException) {
                        false
                    }
                }
            }
        }

        val heightValidator = fun(inputStr: String?) : Boolean {
            if (inputStr == null) return false
            try {
                val height = inputStr.dropLast(2).toInt()
                if (inputStr.endsWith("cm")) {
                    return height in 150..193
                }

                if (inputStr.endsWith("in")) {
                    return height in 59..76
                }
            } catch (e: java.lang.NumberFormatException) {
                return false
            }

            return false
        }
        val hariColorReg = "^#[0-9a-f]{6}$".toRegex()
        val hairColorValidator = fun(inputStr: String?) : Boolean {
            if (inputStr == null) return false
            return hariColorReg.matches(inputStr)
        }

        val eyeColorValidator = fun(inputStr: String?) : Boolean {
            val validEyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            if (inputStr == null) return false
            return validEyeColors.contains(inputStr)
        }

        val pidReg = "^[0-9]{9}$".toRegex()
        val pidValidator = fun(inputStr: String?) : Boolean {
            if (inputStr == null) return false
            return pidReg.matches(inputStr)
        }

        val cidValidator = fun(inputStr: String?) : Boolean {
            return true
        }



        val requiredFields = mapOf(
                "byr" to generateYearRangeValidator(1920, 2002),
                "iyr" to generateYearRangeValidator(2010, 2020),
                "eyr" to generateYearRangeValidator(2020, 2030),
                "hgt" to heightValidator,
                "hcl" to hairColorValidator,
                "ecl" to eyeColorValidator,
                "pid" to pidValidator,
                "cid" to cidValidator,

                )

        val validPassports = passports.filter { p ->
            val fields = p.parameterMap()

            val requiredFound = requiredFields.map {
                it.value(fields[it.key])
            }

            !requiredFound.contains(false)
        }

        println("Valid passport count with validation ${validPassports.size}")

    }
}

fun main() {
    Advent4().calculateAnswer()
}