package ru.ershov.ddd.delivery.core.domain.services

import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order


interface IDispatchService {

    fun chooseCourier(order: Order, couriers: List<Courier>): Courier
}