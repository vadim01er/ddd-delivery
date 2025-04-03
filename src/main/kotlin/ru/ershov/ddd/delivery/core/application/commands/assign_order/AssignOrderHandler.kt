package ru.ershov.ddd.delivery.core.application.commands.assign_order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ershov.ddd.delivery.core.domain.services.DispatchService
import ru.ershov.ddd.delivery.core.ports.ICourierRepository
import ru.ershov.ddd.delivery.core.ports.IOrderRepository

@Service
@Transactional
class AssignOrderHandler(
    private val dispatchService: DispatchService,

    private val orderRepository: IOrderRepository,
    private val courierRepository: ICourierRepository,
) {

    fun handle(command: AssignOrderCommand) {
        val order = orderRepository.anyCreated() ?: return

        val findAllFree = courierRepository.findAllFree().takeIf { it.isNotEmpty() } ?: return

        val chosenCourier = dispatchService.chooseCourier(order, findAllFree)

        order.assignCourier(chosenCourier)
        chosenCourier.busy()

        orderRepository.update(order)
        courierRepository.update(chosenCourier)
    }


}