package fi.solita.advent.hnybom

import java.io.File
import kotlin.math.pow

data class BagRuleRoot(val bagType: String, val contains: List<BagRule>)
data class BagRule(val count: Int, val bagType: String)

class Advent7 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input7.txt").readLines()
    private val bagRegex = "(\\d+)(.*)".toRegex()
    
    fun calculateAnswer() {
        val roots = input.map {
            val split = it.split("contain")
            val container = split[0].trim().replace("bags", "bag")
            val containee = split[1].trim()
            val rules = containee.replace(".","").split(",").flatMap {bag ->
                bagRegex.findAll(bag).map { match ->
                    val values = match.groupValues
                    BagRule(values[1].toInt(), values[2].trim().replace("bags", "bag"))
                }
            }

            BagRuleRoot(container, rules)

        }.groupBy { it.bagType }

        calculateAnswer1(roots)
        calculateAnswer2(roots)
    }

    private fun calculateAnswer1(roots : Map<String, List<BagRuleRoot>>) {

        fun findAllRoots(target: String) : List<BagRuleRoot> {
            val containersThatContainDirectly = roots.keys.flatMap {
                roots[it]?.filter { root -> root.contains.any { rule -> rule.bagType.startsWith(target) } } ?: emptyList()
            }

            return containersThatContainDirectly + containersThatContainDirectly.flatMap { findAllRoots(it.bagType) }

        }
        val shinyContainers = findAllRoots("shiny gold bag").toSet()

        println("Shiny bag containers ${shinyContainers.size}")
    }


    private fun calculateAnswer2(roots :Map<String, List<BagRuleRoot>>) {
        fun calculateTotalBags(bagRuleRoot: BagRuleRoot?, parentCount: Int): Long {
            if(bagRuleRoot == null) return 0L

            val totalBagsOnDepth = bagRuleRoot.contains.fold(0) { acc, bagRule -> acc + bagRule.count }
            val totalPowered = totalBagsOnDepth * parentCount

            val next = bagRuleRoot.contains.map { calculateTotalBags(roots[it.bagType]?.first(), it.count * parentCount) }
            val nextTotal = next.fold(0L) {acc, i -> acc + i}
            return totalPowered + nextTotal


        }
        val total = calculateTotalBags(roots["shiny gold bag"]?.first(), 1)
        println("Shiny bag contains total of $total")
    }
}

fun main() {
    Advent7().calculateAnswer()
}