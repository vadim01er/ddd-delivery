package ru.ershov.ddd.delivery.core.application.queries.get_created_and_assigned_orders

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.infrastructure.adapters.postgres.OrderJpaRepository
import java.util.*

@SpringBootTest
@ActiveProfiles("dev")
class GetCreatedAndAssignedOrdersQueryTest {
    @Autowired
    lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    lateinit var getCreatedAndAssignedOrdersQuery: GetCreatedAndAssignedOrdersQuery

    @BeforeEach
    fun setUp() {
        orderJpaRepository.deleteAll()
    }

    @Test
    fun `find empty orders if empty table`() {
        val query = getCreatedAndAssignedOrdersQuery.query(GetCreatedAndAssignedOrdersRequest())
        assertEquals(0, query.orders.size)
    }

    @Test
    fun `find empty orders if COMPLETED order`() {
        val completedOrder = Order(UUID.randomUUID(), Location.random())
        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        completedOrder.assignCourier(courier)
        completedOrder.complete()
        orderJpaRepository.save(completedOrder)

        val query = getCreatedAndAssignedOrdersQuery.query(GetCreatedAndAssignedOrdersRequest())

        assertEquals(0, query.orders.size)
    }

    @Test
    fun `query find one ASSIGNED order`() {
        val assignOrder = Order(UUID.randomUUID(), Location.random())
        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        assignOrder.assignCourier(courier)
        orderJpaRepository.save(assignOrder)

        val query = getCreatedAndAssignedOrdersQuery.query(GetCreatedAndAssignedOrdersRequest())

        assertEquals(1, query.orders.size)
        assertEquals(assignOrder.id, query.orders[0].id)
    }

    @Test
    fun `query find one CREATED order`() {
        val assignOrder = Order(UUID.randomUUID(), Location.random())
        orderJpaRepository.save(assignOrder)

        val query = getCreatedAndAssignedOrdersQuery.query(GetCreatedAndAssignedOrdersRequest())

        assertEquals(1, query.orders.size)
        assertEquals(assignOrder.id, query.orders[0].id)
    }

}