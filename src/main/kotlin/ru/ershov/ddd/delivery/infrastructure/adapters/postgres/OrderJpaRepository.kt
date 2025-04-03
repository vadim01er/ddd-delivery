package ru.ershov.ddd.delivery.infrastructure.adapters.postgres

import org.springframework.data.jpa.repository.JpaRepository
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import ru.ershov.ddd.delivery.core.ports.IOrderRepository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

interface OrderJpaRepository: JpaRepository<Order, UUID> {

    fun findFirstByStatusIs(status: OrderStatus): Order?

    fun findAllByStatus(status: OrderStatus): List<Order>
}