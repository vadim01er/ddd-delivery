package ru.ershov.ddd.delivery.core.domain.services

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.services.DispatchService
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.*

class DispatchServiceTest {

    private var dispatchService: IDispatchService = DispatchService()

    private val order = Order(UUID.randomUUID(), Location(10, 10))

    @Test
    fun `chooseCourier throw exception when empty list of couriers`() {
        assertThrows(RuntimeException::class.java) { dispatchService.chooseCourier(order, listOf()) }
    }

    @Test
    fun `chooseCourier return correct courier when not empty list of couriers`() {
        val expectedCourier = Courier("name3", "car", 3, Location(1, 1))
        val couriers = listOf(
            Courier("name1", "car", 1, Location(1,1)),
            Courier("name2", "car", 2, Location(1,1)),
            expectedCourier,
        )
        val chooseCourier = dispatchService.chooseCourier(order, couriers)
        assertEquals(expectedCourier, chooseCourier)
    }
}