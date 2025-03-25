package ru.ershov.ddd.delivery.core.ports

import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import java.util.UUID

interface IOrderRepository {
    fun add(order: Order): Order

    fun update(order: Order): Order

    fun byId(id: UUID): Order?

    fun anyCreated(): Order?

    fun allAssigned(): List<Order>
}