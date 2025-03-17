package ru.ershov.ddd.delivery.core.domain.services

import org.springframework.stereotype.Service
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order

@Service
class DispatchService : IDispatchService {

    override fun chooseCourier(order: Order, couriers: List<Courier>): Courier {
        require(couriers.isNotEmpty()) { "List of Couriers must not be empty" }
        val targetLocation = order.location

        val chosenCourier = couriers
            .minByOrNull { courier -> courier.timeToLocation(targetLocation) }!!  // потому что проверили что не пустой список выше

        order.assignCourier(chosenCourier)
        chosenCourier.busy()

        return chosenCourier
    }

}