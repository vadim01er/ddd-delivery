package ru.ershov.ddd.delivery.infrastructure.adapters.postgres

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.*

@SpringBootTest
@ActiveProfiles("dev")
class OrderRepositoryTest {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @BeforeEach
    fun setup() {
        orderJpaRepository.deleteAll()
    }

    @Test
    fun `add should save and retrieve the order correctly`() {
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val savedOrder = orderRepository.add(order)

        assertNotNull(savedOrder.id)
        assertEquals(OrderStatus.CREATED, savedOrder.status)

        val retrievedOrder = orderRepository.byId(savedOrder.id)
        assertEquals(savedOrder, retrievedOrder)
    }

    @Test
    fun `update should update and retrieve the order correctly`() {
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val savedOrder = orderRepository.add(order)

        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        savedOrder.assignCourier(courier)

        val updatedOrder = orderRepository.update(savedOrder)

        assertEquals(OrderStatus.ASSIGNED, updatedOrder.status)

        val retrievedOrder = orderRepository.byId(updatedOrder.id)
        assertEquals(updatedOrder, retrievedOrder)
    }

    @Test
    fun `byId should return null if order is not found`() {
        val id = UUID.randomUUID()
        val retrievedOrder = orderRepository.byId(id)
        assertNull(retrievedOrder)
    }

    @Test
    fun `byId should retrieve the order by id correctly`() {
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val savedOrder = orderRepository.add(order)

        val retrievedOrder = orderRepository.byId(savedOrder.id)
        assertEquals(savedOrder, retrievedOrder)
    }

    @Test
    fun `anyCreated should retrieve any order with status CREATED correctly`() {
        val order1 = Order(UUID.randomUUID(), Location(1, 1))
        val order2 = Order(UUID.randomUUID(), Location(2, 2))
        order2.assignCourier(Courier("Курьер1", "Машины", 3, Location(1, 1)))

        orderRepository.add(order1)
        orderRepository.add(order2)

        val createdOrder = orderRepository.anyCreated()
        assertNotNull(createdOrder)
        assertEquals(OrderStatus.CREATED, createdOrder?.status)
    }

    @Test
    fun `allAssigned should retrieve all orders with status ASSIGNED correctly`() {
        val order1 = Order(UUID.randomUUID(), Location(1, 1))
        val order2 = Order(UUID.randomUUID(), Location(2, 2))
        val order3 = Order(UUID.randomUUID(), Location(3, 3))

        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        order1.assignCourier(courier)
        order2.assignCourier(courier)

        orderRepository.add(order1)
        orderRepository.add(order2)
        orderRepository.add(order3)

        val assignedOrders = orderRepository.allAssigned()
        assertEquals(2, assignedOrders.size)
        assertTrue(assignedOrders.contains(order1))
        assertTrue(assignedOrders.contains(order2))
    }
}