package ru.ershov.ddd.delivery.infrastructure.adapters.postgres

import org.springframework.stereotype.Component
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import ru.ershov.ddd.delivery.core.ports.IOrderRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Component
internal class OrderRepository(
    private val orderJpaRepository: OrderJpaRepository
): IOrderRepository {
    override fun add(order: Order): Order =
        orderJpaRepository.save(order)

    override fun update(order: Order): Order =
        orderJpaRepository.save(order)

    override fun byId(id: UUID): Order? =
        orderJpaRepository.findById(id).getOrNull()

    override fun anyCreated(): Order? =
        orderJpaRepository.findFirstByStatusIs(OrderStatus.CREATED)

    override fun allAssigned(): List<Order> =
        orderJpaRepository.findAllByStatus(OrderStatus.ASSIGNED)
}