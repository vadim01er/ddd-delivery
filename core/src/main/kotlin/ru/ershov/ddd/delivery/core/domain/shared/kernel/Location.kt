package ru.ershov.ddd.delivery.core.domain.shared.kernel

import kotlin.math.abs
import kotlin.random.Random

private const val MIN_VALUE = 1
private const val MAX_VALUE = 10

// Value Object
data class Location(
    val x: Int,
    val y: Int
) {

    init {
        require(x in MIN_VALUE..MAX_VALUE) { "Value X should be within the boundaries from $MIN_VALUE to $MAX_VALUE" }
        require(y in MIN_VALUE..MAX_VALUE) { "Value Y should be within the boundaries from $MIN_VALUE to $MAX_VALUE" }
    }


    fun distance(location: Location): Int {
        val dx = abs(x - location.x)
        val dy = abs(y - location.y)
        return dx + dy
    }

    companion object {
        fun random(): Location {
            val x = Random.nextInt(0, MAX_VALUE)
            val y = Random.nextInt(0, MAX_VALUE)
            return Location(x, y)
        }
    }

}
