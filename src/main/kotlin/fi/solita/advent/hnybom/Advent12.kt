package fi.solita.advent.hnybom

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

enum class Facing(private val id: Int) {
    EAST(0), SOUTH(1), WEST(2), NORTH(3), LEFT(-1), RIGHT(-1), FORWARD(-1);

    fun turnRight(steps: Int): Facing {
        val newDirection = (steps + id) % 4
        return values().find { it.id == newDirection } ?: throw IllegalArgumentException("WTF")
    }

    fun turnLeft(steps: Int): Facing {
        val newDirection = abs(id - steps + 4) % 4
        return values().find { it.id == newDirection } ?: throw IllegalArgumentException("WTF left")
    }

    companion object {
        fun fromChar(char: Char): Facing {
            return when(char) {
                'E' -> EAST
                'N' -> NORTH
                'W' -> WEST
                'S' -> SOUTH
                'L' -> LEFT
                'R' -> RIGHT
                'F' -> FORWARD
                else -> throw IllegalArgumentException("Char $char is not valid direction")
            }
        }
    }

}
data class Command(val amount: Int, val command: Facing)

data class Waypoint(var east: Int, var north: Int) {

    fun turnRight(angle: Int) {
        turn(-angle)

    }

    fun turnLeft(angle: Int) {
        turn(angle)
    }

    private fun turn(angle: Int) {
        val s = sin(Math.toRadians(angle.toDouble()))
        val c = cos(Math.toRadians(angle.toDouble()))

        val newEast = east * c - north * s
        val newNorth = east * s + north * c
        east = newEast.roundToInt()
        north = newNorth.roundToInt()
    }

}

data class Ship(var east: Int, var north: Int, var facing: Facing)
data class Ship2(var east: Int, var north: Int, var waypoint: Waypoint)

class Advent12 {

    private val input = File("/home/henriny/work/own/advent/src/main/resources/input12.txt")
            .readLines().map {
                Command(it.substring(1).toInt(), Facing.fromChar(it[0]))
            }

    fun calculateAnswer() {

        calculateAnswer1()
        calculateAnswer2()
    }

    private fun calculateAnswer1() {
        fun handleMove(direction : Facing, amount: Int, ship: Ship) {
            when (direction) {
                Facing.EAST -> ship.east = ship.east + amount
                Facing.WEST -> ship.east = ship.east - amount
                Facing.SOUTH -> ship.north = ship.north - amount
                Facing.NORTH -> ship.north = ship.north + amount
            }
        }

        val ship = Ship(0, 0, Facing.EAST)
        input.forEach {
            when(it.command) {
                Facing.RIGHT -> {
                    ship.facing = ship.facing.turnRight(it.amount / 90)
                }
                Facing.LEFT -> {
                    ship.facing = ship.facing.turnLeft(it.amount / 90)
                }
                Facing.FORWARD -> handleMove(ship.facing, it.amount, ship)
                else -> handleMove(it.command, it.amount, ship)
            }
        }

        println("End position $ship and manhattan distance ${abs(ship.east) + abs(ship.north)}")

    }


    private fun calculateAnswer2() {

        val ship = Ship2(0,0, Waypoint(10, 1))

        fun handleWaypointMove(direction : Facing, amount: Int, ship: Ship2) {
            when (direction) {
                Facing.EAST -> ship.waypoint.east = ship.waypoint.east + amount
                Facing.WEST -> ship.waypoint.east = ship.waypoint.east - amount
                Facing.SOUTH -> ship.waypoint.north = ship.waypoint.north - amount
                Facing.NORTH -> ship.waypoint.north = ship.waypoint.north + amount
            }
        }

        fun handleShip2Move(amount: Int, ship: Ship2) {
            ship.east = ship.east + (ship.waypoint.east * amount)
            ship.north = ship.north + (ship.waypoint.north * amount)
        }

        input.forEach {
            when(it.command) {
                Facing.RIGHT -> {
                    ship.waypoint.turnRight(it.amount)
                }
                Facing.LEFT -> {
                    ship.waypoint.turnLeft(it.amount)
                }
                Facing.FORWARD -> handleShip2Move(it.amount, ship)
                else -> handleWaypointMove(it.command, it.amount, ship)
            }
        }

        println("End position for ship2 $ship and manhattan distance ${abs(ship.east) + abs(ship.north)}")
    }

}

fun main() {
    Advent12().calculateAnswer()
}
