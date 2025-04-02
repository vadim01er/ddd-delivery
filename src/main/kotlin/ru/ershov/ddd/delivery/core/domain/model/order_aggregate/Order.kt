package ru.ershov.ddd.delivery.core.domain.model.order_aggregate

import jakarta.persistence.*
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.UUID

@Entity
@Table(name = "orders")
class Order {
    @Id
    val id: UUID
    @Embedded
    val location: Location

    @Enumerated(EnumType.STRING)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}