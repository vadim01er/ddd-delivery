package ru.ershov.ddd.delivery.core.domain.model.courier_aggregate

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location

class CourierTest {

    @Test
    fun `constructor throw exception when incorrect parameters`() {
        assertThrows(IllegalArgumentException::class.java) { Courier("", "name", 1, Location.random()) }
    }

    @Test
    fun `constructor return Courier when correct parameters`() {
        val courier = Courier("name", "name", 1, Location.random())
        assertEquals("name", courier.name)
        assertEquals("name", courier.transport.name)
        assertEquals(1, courier.transport.speed)
        assertNotNull(courier.location)
    }

    @Test
    fun `busy throw exception when incorrect status`() {
        val courier = Courier("name", "name", 1, Location.random())
        courier.busy()
        assertThrows(IllegalArgumentException::class.java) { courier.busy() }
    }

    @Test
    fun `busy return ok when correct status`() {
        val courier = Courier("name", "name", 1, Location.random())
        courier.busy()
        assertEquals(CourierStatus.BUSY, courier.status)
    }

    @Test
    fun `free throw exception when incorrect status`() {
        val courier = Courier("name", "name", 1, Location.random())
        assertThrows(IllegalArgumentException::class.java) { courier.free() }
    }

    @Test
    fun `free return ok when correct status`() {
        val courier = Courier("name", "name", 1, Location.random())
        courier.busy()
        courier.free()
        assertEquals(CourierStatus.FREE, courier.status)
    }

    @Test
    fun `timeToLocation return ok`() {
        val courier = Courier("name", "name", 2, Location(1, 1))
        assertEquals(1, courier.timeToLocation(Location(1, 2)))
        assertEquals(1, courier.timeToLocation(Location(2, 1)))
        assertEquals(9, courier.timeToLocation(Location(10, 10)))
        assertEquals(2, courier.timeToLocation(Location(1, 4)))
    }

    @Test
    fun `move return ok`() {
        val location = Location(1, 1)
        val courier = Courier("name", "name", 2, location)
        courier.move(Location(10, 10))
        assertNotEquals(location, courier.location)
    }
}