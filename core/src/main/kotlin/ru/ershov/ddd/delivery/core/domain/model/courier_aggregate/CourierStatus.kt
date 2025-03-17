package ru.ershov.ddd.delivery.core.domain.model.courier_aggregate

class CourierStatus private constructor(val name: String){

    companion object {
        val FREE = CourierStatus("FREE")
        val BUSY = CourierStatus("BUSY")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CourierStatus

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}