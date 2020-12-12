package fi.solita.advent.hnybom

import java.io.File
import java.lang.IllegalStateException

typealias Coordinate = Pair<Int, Int>

enum class GridState{
    FLOOR, EMPTY, OCCUPIED;

    companion object {

        fun fromChar(char: Char): GridState {
            return when (char) {
                '.' -> FLOOR
                'L' -> EMPTY
                '#' -> OCCUPIED
                else -> throw IllegalStateException("Invalid state char $char")
            }
        }
    }
}

data class GridCell(val row: Int, val column: Int, var state: GridState) {

    fun getAdjacentCoordinates(maxRowIndex: Int, maxColumnIndex: Int) : List<Coordinate> {
        return ((row - 1)..(row + 1)).flatMap { row ->
            ((column - 1)..(column + 1)).map {
                Coordinate(it, row)
            }
        }.filter { (it.first >= 0 && it.second >= 0) && (it.first <= maxColumnIndex && it.second <= maxRowIndex) }
    }

}

data class Grid(val gridCells: Map<Coordinate, GridCell>) {

    val maxColumnIndex: Int = gridCells.maxOf { it.value.column }
    val maxRowIndex: Int = gridCells.maxOf { it.value.row }

    fun findToBeOccupied() : List<GridCell> {
        return gridCells.filter { entry ->
            val adjacentGrids = getAdjacent(entry)
            entry.value.state == GridState.EMPTY && adjacentGrids.all { it?.state == GridState.EMPTY }
        }.map { it.value }
    }

    fun findToBeReleased() : List<GridCell> {
        return gridCells.filter { entry ->
            val adjacentGrids = getAdjacent(entry)
            entry.value.state == GridState.OCCUPIED && adjacentGrids.filter { it?.state == GridState.OCCUPIED }.size > 3
        }.map { it.value }
    }

    fun findToBeOccupiedByView() : List<GridCell> {
        return gridCells.filter { entry ->
            val inView = getInView(entry)
            entry.value.state == GridState.EMPTY && inView.filterNotNull().all { it?.state == GridState.EMPTY }
        }.map { it.value }
    }

    fun findToBeReleasedByView() : List<GridCell> {
        return gridCells.filter { entry ->
            val inView = getInView(entry)
            entry.value.state == GridState.OCCUPIED && inView.filterNotNull().filter { it?.state == GridState.OCCUPIED }.size > 4
        }.map { it.value }
    }


    private fun getAdjacent(entry: Map.Entry<Coordinate, GridCell>): List<GridCell?> {
        val coords = entry.value.getAdjacentCoordinates(maxRowIndex, maxColumnIndex)
        return coords.map { gridCells[it] }.filter { it?.state != GridState.FLOOR }

    }

    private fun getInView(entry: Map.Entry<Coordinate, GridCell>): List<GridCell?> {
        val down = (entry.value.row + 1)..maxRowIndex
        val up = (entry.value.row - 1) downTo 0
        val right = (entry.value.column + 1)..maxColumnIndex
        val left = (entry.value.column - 1) downTo 0

        val firstToRight = right.map {
            gridCells[Coordinate(it, entry.value.row)]
        }.find { it?.state != GridState.FLOOR }

        val firstToLeft = left.map {
            gridCells[Coordinate(it, entry.value.row)]
        }.find { it?.state != GridState.FLOOR }

        val firstToDown = down.map {
            gridCells[Coordinate(entry.value.column, it)]
        }.find { it?.state != GridState.FLOOR }

        val firstToUp = up.map {
            gridCells[Coordinate(entry.value.column, it)]
        }.find { it?.state != GridState.FLOOR }

        val rd = right.zip(down).map {
            gridCells[it]
        }.find { it?.state != GridState.FLOOR }

        val ru = right.zip(up).map {
            gridCells[it]
        }.find { it?.state != GridState.FLOOR }

        val lu = left.zip(up).map {
            gridCells[it]
        }.find { it?.state != GridState.FLOOR }

        val ld = left.zip(down).map {
            gridCells[it]
        }.find { it?.state != GridState.FLOOR }

        return listOf(firstToRight, firstToLeft, firstToDown, firstToUp, rd, ru, lu, ld)

    }
}

class Advent11 {

    private val input = File("/home/henriny/work/AdventOfCode2020/src/main/resources/input11.txt")
            .readLines().flatMapIndexed { rowIndex, s ->
                s.mapIndexed { index, c ->  GridCell(rowIndex, index, GridState.fromChar(c)) }
            }

    fun calculateAnswer() {
        val grid = Grid(
                gridCells = input.map {
                    Coordinate(it.column, it.row) to it
                }.toMap()
        )

        //calculateAnswer1(grid)
        calculateAnswer2(grid)
    }

    private fun calculateAnswer1(grid: Grid) {
        while (true) {
            val findToBeOccupied = grid.findToBeOccupied()
            val findToBeReleased = grid.findToBeReleased()

            findToBeOccupied.forEach {
                it.state = GridState.OCCUPIED
            }

            findToBeReleased.forEach {
                it.state = GridState.EMPTY
            }

            if(findToBeOccupied.isEmpty() && findToBeReleased.isEmpty()) {
                val occupied = grid.gridCells.filter { it.value.state == GridState.OCCUPIED }.size
                println("End state $occupied")
                break
            }
        }

    }


    private fun calculateAnswer2(grid: Grid) {
        while (true) {
            val findToBeOccupied = grid.findToBeOccupiedByView()
            val findToBeReleased = grid.findToBeReleasedByView()

            findToBeOccupied.forEach {
                it.state = GridState.OCCUPIED
            }

            findToBeReleased.forEach {
                it.state = GridState.EMPTY
            }

            if(findToBeOccupied.isEmpty() && findToBeReleased.isEmpty()) {
                val occupied = grid.gridCells.filter { it.value.state == GridState.OCCUPIED }.size
                println("End state part two $occupied")
                break
            }
        }
    }
}

fun main() {
    Advent11().calculateAnswer()
}