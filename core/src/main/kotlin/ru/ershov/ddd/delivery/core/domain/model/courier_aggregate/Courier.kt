package ru.ershov.ddd.delivery.core.domain.model.courier_aggregate

import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.UUID

class Courier {
    val id: UUID
    val name: String
    val transport: Transport

    var location: Location
        private set
    var status: CourierStatus
        private set

    constructor(name: String, transportName: String, transportSpeed: Int, location: Location) {
        require(name.isNotBlank()) { "Name must not be blank" }
        this.id = UUID.randomUUID()
        this.name = name
        this.transport = Transport(transportName, transportSpeed)
        this.location = location
        this.status = CourierStatus.FREE
    }

    fun busy() {
        require(this.status == CourierStatus.FREE) { "Courier is already ${CourierStatus.BUSY}" }
        this.status = CourierStatus.BUSY
    }

    fun free() {
        require(this.status == CourierStatus.BUSY) { "Courier is already ${CourierStatus.FREE}" }
        this.status = CourierStatus.FREE
    }

    fun timeToLocation(location: Location): Int {
        val distance = this.location.distance(location)
        return (distance + transport.speed - 1) / transport.speed
    }

    fun move(orderLocation: Location) {
        val currentLocation = transport.move(this.location, orderLocation)
        this.location = currentLocation
    }


}