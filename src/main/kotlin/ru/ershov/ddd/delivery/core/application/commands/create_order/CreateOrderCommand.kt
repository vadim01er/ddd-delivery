package ru.ershov.ddd.delivery.core.application.commands.create_order

import java.util.UUID

class CreateOrderCommand(
    val basketId: UUID,
    val address: String,
) {
    init {
        require(address.isNotEmpty()) { "Address must not be empty" }
    }
}