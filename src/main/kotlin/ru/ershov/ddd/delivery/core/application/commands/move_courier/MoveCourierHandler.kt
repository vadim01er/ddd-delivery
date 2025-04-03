package ru.ershov.ddd.delivery.core.application.commands.move_courier

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ershov.ddd.delivery.core.ports.ICourierRepository
import ru.ershov.ddd.delivery.core.ports.IOrderRepository

@Service
@Transactional
class MoveCourierHandler(
    private val orderRepository: IOrderRepository,
    private val courierRepository: ICourierRepository
) {

    fun handle(command: MoveCourierCommand) {
        orderRepository.allAssigned().forEach { order ->
            val courier = courierRepository.findBy(order.courierId)
                ?: throw RuntimeException("courier with id ${order.courierId} not found")

            courier.move(order.location)

            if (courier.location == order.location) {
                order.complete()
                courier.free()
            }

            orderRepository.update(order)
            courierRepository.update(courier)
        }
    }
}