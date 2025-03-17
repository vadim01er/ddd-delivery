package ru.ershov.ddd.delivery.core.domain.model.order_aggregate

import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.UUID

class Order {
    val id: UUID
    val location: Location

    var status: OrderStatus
        private set
    lateinit var courierId: UUID
        private set

    constructor(id: UUID, location: Location) {
        this.id = id
        this.location = location
        this.status = OrderStatus.CREATED
    }

    fun assignCourier(courier: Courier) {
        require(status == OrderStatus.CREATED) { "Order must be in ${OrderStatus.CREATED} status" }
        courierId = courier.id
        status = OrderStatus.ASSIGNED
    }

    fun complete() {
        require(status == OrderStatus.ASSIGNED) { "Order must be in ${OrderStatus.ASSIGNED} status" }

        status = OrderStatus.COMPLETED
    }


}