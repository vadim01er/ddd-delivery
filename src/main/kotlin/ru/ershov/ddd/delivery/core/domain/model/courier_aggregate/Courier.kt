package ru.ershov.ddd.delivery.core.domain.model.courier_aggregate

import jakarta.persistence.*
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.UUID

@Entity
@Table(name = "couriers")
class Courier {
    @Id
    val id: UUID
    val name: String
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "transport_id")
    val transport: Transport

    @Embedded
    var location: Location
        private set
    @Enumerated(EnumType.STRING)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Courier

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Courier(id=$id, name='$name', transport=$transport, location=$location, status=$status)"
    }


}