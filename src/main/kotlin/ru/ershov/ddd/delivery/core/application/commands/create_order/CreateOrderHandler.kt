package ru.ershov.ddd.delivery.core.application.commands.create_order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.ports.IGeoClient
import ru.ershov.ddd.delivery.core.ports.IOrderRepository

@Service
@Transactional
class CreateOrderHandler(
    private val orderRepository: IOrderRepository,
    private val geoClient: IGeoClient
) {

    fun handle(command: CreateOrderCommand) {
        orderRepository.byId(command.basketId)?.let {
            throw RuntimeException("Order with id ${command.basketId} already exists.")
        }

        val location = geoClient.getBy(command.address)

        val order = Order(
            id = command.basketId,
            location = location
        )

        orderRepository.add(order)
    }
}