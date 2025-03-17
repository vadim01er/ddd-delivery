package ru.ershov.ddd.delivery.core.domain.model.courier

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.stream.Stream

class TransportTest {


    @ParameterizedTest
    @CsvSource(
        "'', -1",
        "' ', 4"
    )
    fun `throw exception when params is invalid`(name: String, speed: Int) {
        assertThrows(IllegalArgumentException::class.java) { Transport(name, speed) }
    }

    @ParameterizedTest
    @CsvSource(
        "name1, 1",
        "name2, 2",
        "name3, 3"
    )
    fun `should by success create when params is valid`(name: String, speed: Int) {
        val transport = Transport(name, speed)

        assertEquals(name, transport.name)
        assertEquals(speed, transport.speed)
    }



    companion object {
        @JvmStatic
        fun moveTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(Location(1, 1), Location(5, 5), 1, Location(2, 1)),
            Arguments.of(Location(5, 1), Location(1, 5), 1, Location(4, 1)),
            Arguments.of(Location(1, 1), Location(1, 5), 1, Location(1, 2)),
            Arguments.of(Location(1, 5), Location(1, 1), 1, Location(1, 4)),
            Arguments.of(Location(1, 1), Location(5, 5), 2, Location(3, 1)),
            Arguments.of(Location(1, 1), Location(5, 3), 3, Location(4, 1)),
            Arguments.of(Location(1, 1), Location(2, 4), 3, Location(2, 3)),
            Arguments.of(Location(3, 3), Location(3, 3), 1, Location(3, 3)),
            Arguments.of(Location(1, 1), Location(10, 10), MAX_SPEED, Location(4, 1)),
            Arguments.of(Location(1, 1), Location(2, 2), MIN_SPEED, Location(2, 1)),
            Arguments.of(Location(1, 1), Location(3, 4), 3, Location(3, 2)),
            Arguments.of(Location(1, 1), Location(2, 5), 2, Location(2, 2))
        )
    }

    @ParameterizedTest
    @MethodSource("moveTestCases")
    fun move(current: Location, target: Location, speed: Int, expected: Location) {
        val transport = Transport("Car", speed)
        val result = transport.move(current, target)
        assertEquals(expected, result)
    }
}