package ru.ershov.ddd.delivery.api.adapters.http

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.ershov.ddd.delivery.api.adapters.generated.apis.DefaultApi
import ru.ershov.ddd.delivery.api.adapters.generated.models.Courier
import ru.ershov.ddd.delivery.api.adapters.generated.models.Location
import ru.ershov.ddd.delivery.api.adapters.generated.models.Order
import ru.ershov.ddd.delivery.core.application.commands.create_order.CreateOrderCommand
import ru.ershov.ddd.delivery.core.application.commands.create_order.CreateOrderHandler
import ru.ershov.ddd.delivery.core.application.queries.get_couriers.GetCouriersQuery
import ru.ershov.ddd.delivery.core.application.queries.get_couriers.GetCouriersRequest
import ru.ershov.ddd.delivery.core.application.queries.get_couriers.GetCouriersResponse
import ru.ershov.ddd.delivery.core.application.queries.get_created_and_assigned_orders.GetCreatedAndAssignedOrdersQuery
import ru.ershov.ddd.delivery.core.application.queries.get_created_and_assigned_orders.GetCreatedAndAssignedOrdersRequest
import ru.ershov.ddd.delivery.core.application.queries.get_created_and_assigned_orders.GetCreatedAndAssignedOrdersResponse
import java.util.*

@RestController
class Controller(
    private val createOrderHandler: CreateOrderHandler,

    private val getCouriersQuery: GetCouriersQuery,
    private val getCreatedAndAssignedOrdersQuery: GetCreatedAndAssignedOrdersQuery
) : DefaultApi {

    override fun createOrder(): ResponseEntity<Unit> {
        return createOrderHandler.handle(CreateOrderCommand(UUID.randomUUID(), "SOME ADDRESS"))
            .let { ResponseEntity.status(HttpStatus.CREATED).build() }
    }

    override fun getCouriers(): ResponseEntity<List<Courier>> {
        return getCouriersQuery.query(GetCouriersRequest())
            .let { it.couriers.map { courier -> courier.toCourierResponse() } }
            .let { ResponseEntity.ok(it) }
    }

    private fun GetCouriersResponse.Courier.toCourierResponse() =
        Courier(
            id = this.id,
            name = this.name,
            location = Location(
                this.location.x,
                this.location.y
            )
        )

    override fun getOrders(): ResponseEntity<List<Order>> {
        return getCreatedAndAssignedOrdersQuery.query(GetCreatedAndAssignedOrdersRequest())
            .let { it.orders.map { order -> order.getOrderResponse() } }
            .let { ResponseEntity.ok(it) }
    }

    private fun GetCreatedAndAssignedOrdersResponse.Order.getOrderResponse() =
        Order(
            id = this.id,
            location = Location(
                this.location.x,
                this.location.y
            )
        )

}