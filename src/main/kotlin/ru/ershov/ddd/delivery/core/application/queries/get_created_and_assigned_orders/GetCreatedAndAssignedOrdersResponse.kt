package ru.ershov.ddd.delivery.core.application.queries.get_created_and_assigned_orders

import java.util.UUID


data class GetCreatedAndAssignedOrdersResponse(
    val orders: List<Order>
) {
    data class Order(
        val id: UUID,
        var location: Location
    ) {

        data class Location (
            val x: Int,
            val y: Int,
        )
    }
}