package ru.ershov.ddd.delivery.core.domain.shared.kernel

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location


class LocationTest {

    @ParameterizedTest
    @CsvSource(
        "0, 0",
        "11, 11"
    )
    fun `throw exception when params is invalid`(x: Int, y: Int) {
        assertThrows(IllegalArgumentException::class.java) { Location(x, y) }
    }

    @ParameterizedTest
    @CsvSource(
        "1, 1",
        "5, 6",
        "10, 10"
    )
    fun `should by create location when params is valid`(x: Int, y: Int) {
        val location = Location(x, y)

        assertEquals(x, location.x)
        assertEquals(y, location.y)
    }

    @ParameterizedTest
    @CsvSource(
        "4, 9, 2, 6,  5",
        "1, 1, 1, 1,  0",
        "1, 1, 2, 1,  1",
        "1, 1, 1, 2,  1",
    )
    fun `can correct calculate distance`(x1: Int, y1: Int, x2: Int, y2: Int, expected: Int) {
        val location1 = Location(x1, y1)
        val location2 = Location(x2, y2)

        assertEquals(expected, location1.distance(location2))
    }

    @Test
    fun `can generate random location` () {
        val random = Location.random()

        assertTrue(random.x in 1..10)
        assertTrue(random.y in 1..10)
    }


}