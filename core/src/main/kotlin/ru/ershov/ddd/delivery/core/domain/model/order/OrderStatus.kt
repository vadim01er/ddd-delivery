package ru.ershov.ddd.delivery.core.domain.model.order


class OrderStatus private constructor(val name: String) {

    companion object {
        val CREATED = OrderStatus("CREATED")
        val ASSIGNED = OrderStatus("ASSIGNED")
        val COMPLETED = OrderStatus("COMPLETED")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderStatus

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}