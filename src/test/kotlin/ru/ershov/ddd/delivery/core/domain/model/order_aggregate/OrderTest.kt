package ru.ershov.ddd.delivery.core.domain.model.order_aggregate

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.*
import kotlin.test.assertEquals

class OrderTest {

    val courier = Courier("name", "name", 1, Location.random())

    @Test
    fun `assignCourier success`() {
        val order = Order(UUID.randomUUID(), Location.random())
        order.assignCourier(courier)

        assertEquals(OrderStatus.ASSIGNED, order.status)
        assertNotNull(order.courierId)
    }

    @Test
    fun `assignCourier throw exception when status is ASSIGNED`() {
        val order = Order(UUID.randomUUID(), Location.random())

        order.assignCourier(courier)

        assertThrows(IllegalArgumentException::class.java) {order.assignCourier(courier)}
    }


    @Test
    fun `assignCourier throw exception when status is COMPLETED`() {
        val order = Order(UUID.randomUUID(), Location.random())

        order.assignCourier(courier)
        order.complete()

        assertThrows(IllegalArgumentException::class.java) {order.assignCourier(courier)}
    }

    @Test
    fun `complete success`() {
        val order = Order(UUID.randomUUID(), Location.random())
        order.assignCourier(courier)
        order.complete()
        assertEquals(OrderStatus.COMPLETED, order.status)
    }

    @Test
    fun `complete throw exception when status is CREATED`() {
        val order = Order(UUID.randomUUID(), Location.random())
        assertThrows(IllegalArgumentException::class.java) {order.complete()}
    }

    @Test
    fun `complete throw exception when status is COMPLETED`() {
        val order = Order(UUID.randomUUID(), Location.random())
        order.assignCourier(courier)
        order.complete()
        assertThrows(IllegalArgumentException::class.java) {order.complete()}
    }


}