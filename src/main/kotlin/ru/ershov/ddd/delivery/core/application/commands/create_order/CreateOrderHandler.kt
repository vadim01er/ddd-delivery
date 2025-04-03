package ru.ershov.ddd.delivery.core.application.commands.create_order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.core.ports.IOrderRepository

@Service
@Transactional
class CreateOrderHandler(
    private val orderRepository: IOrderRepository
) {

    fun handle(command: CreateOrderCommand) {
        orderRepository.byId(command.basketId)?.let {
            throw RuntimeException("Order with id ${command.basketId} already exists.")
        }

        val order = Order(
            id = command.basketId,
            location = Location.random()
        )

        orderRepository.add(order)
    }
}