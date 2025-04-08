package ru.ershov.ddd.delivery.core.application.commands.assign_order

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import ru.ershov.ddd.delivery.core.domain.services.DispatchService
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.core.ports.ICourierRepository
import ru.ershov.ddd.delivery.core.ports.IOrderRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
class AssignOrderHandlerTest {

    @InjectMocks
    lateinit var handler: AssignOrderHandler

    @Mock
    lateinit var dispatchService: DispatchService
    @Mock
    lateinit var orderRepository: IOrderRepository
    @Mock
    lateinit var courierRepository: ICourierRepository

    @Test
    fun `do nothing if empty CREATED order`() {
        whenever(orderRepository.anyCreated())
            .thenReturn(null)

        handler.handle(AssignOrderCommand())
    }

    @Test
    fun `do nothing if one CREATED order and empty FREE courier`() {
        whenever(orderRepository.anyCreated())
            .thenReturn(Order(UUID.randomUUID(), Location.random()))

        whenever(courierRepository.findAllFree())
            .thenReturn(emptyList())

        handler.handle(AssignOrderCommand())
    }

    @Test
    fun `assign if one CREATED order and one FREE courier`() {
        whenever(orderRepository.anyCreated())
            .thenReturn(Order(UUID.randomUUID(), Location.random()))

        val savedCourier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        whenever(courierRepository.findAllFree())
            .thenReturn(listOf(savedCourier))

        whenever(dispatchService.chooseCourier(any(), any()))
            .thenAnswer { (it.arguments[1] as List<*>).first() }

        val courier = argumentCaptor<Courier>()
        val order = argumentCaptor<Order>()
        whenever(courierRepository.update(courier.capture()))
            .thenAnswer { it.arguments[0] }
        whenever(orderRepository.update(order.capture()))
            .thenAnswer { it.arguments[0] }

        handler.handle(AssignOrderCommand())

        verify(orderRepository).update(any())
        verify(courierRepository).update(any())

        assertEquals(savedCourier.id, order.firstValue.courierId)
        assertEquals(OrderStatus.ASSIGNED, order.firstValue.status)
        assertEquals(CourierStatus.BUSY, courier.firstValue.status)
    }
}