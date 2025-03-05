package ru.ershov.ddd.delivery.core.domain.shared.kernel

import kotlin.math.abs
import kotlin.random.Random

data class Location(
    val x: Int,
    val y: Int
) {

    init {
        require(x in 1..10) { "Value X should be within the boundaries from 0 to 9" }
        require(y in 1..10) { "Value Y should be within the boundaries from 0 to 9" }
    }


    fun distance(location: Location): Int {
        val dx = abs(x - location.x)
        val dy = abs(y - location.y)
        return dx + dy
    }

    companion object {
        fun random(): Location {
            val x = Random.nextInt(0, 10)
            val y = Random.nextInt(0, 10)
            return Location(x, y)
        }
    }

}
