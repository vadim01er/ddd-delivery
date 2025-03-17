package ru.ershov.ddd.delivery.core.domain.model.courier

import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.UUID
import kotlin.math.abs

const val MIN_SPEED = 1
const val MAX_SPEED = 3


class Transport private constructor(
    val id: UUID,
    var name: String,
    var speed: Int
) {

    init {
        require(name.isNotBlank()) { "Value name shouldn't be blank" }
        require(speed in MIN_SPEED..MAX_SPEED) { "Value speed should be within the boundaries from $MIN_SPEED to $MAX_SPEED" }
    }

    constructor(name: String, speed: Int) : this(UUID.randomUUID(), name, speed)

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
