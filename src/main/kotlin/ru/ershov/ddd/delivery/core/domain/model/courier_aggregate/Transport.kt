package ru.ershov.ddd.delivery.core.domain.model.courier_aggregate

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.UUID
import kotlin.math.abs

const val MIN_SPEED = 1
const val MAX_SPEED = 3

@Entity
@Table(name = "transports")
class Transport {

    @Id
    val id: UUID

    var name: String
        private set
    var speed: Int
        private set

    constructor(name: String, speed: Int) {
        require(name.isNotBlank()) { "Value name shouldn't be blank" }
        require(speed in MIN_SPEED..MAX_SPEED) { "Value speed should be within the boundaries from $MIN_SPEED to $MAX_SPEED" }
        this.id = UUID.randomUUID()
        this.name = name
        this.speed = speed
    }

    fun move(current: Location, target: Location): Location {
        val dx = (target.x - current.x).coerceIn(-speed, speed)
        val remainingSpeed = speed - abs(dx)
        val dy = (target.y - current.y).coerceIn(-remainingSpeed, remainingSpeed)
        return Location(current.x + dx, current.y + dy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transport

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
