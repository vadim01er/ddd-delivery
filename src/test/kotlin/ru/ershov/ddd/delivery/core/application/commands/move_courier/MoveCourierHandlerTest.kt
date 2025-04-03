package ru.ershov.ddd.delivery.core.application.commands.move_courier

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.core.ports.ICourierRepository
import ru.ershov.ddd.delivery.core.ports.IOrderRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
class MoveCourierHandlerTest {

    @InjectMocks
    lateinit var moveCourierHandler: MoveCourierHandler

    @Mock
    lateinit var orderRepository: IOrderRepository

    @Mock
    lateinit var courierRepository: ICourierRepository

    @Test
    fun `do nothing if empty ASSIGN orders`() {
        whenever(orderRepository.allAssigned())
            .thenReturn(emptyList())

        moveCourierHandler.handle(MoveCourierCommand())

        verify(orderRepository, never()).update(any())
        verify(courierRepository, never()).update(any())
    }

    @Test
    fun `throw exception if one ASSIGN orders but courier not exist`() {
        val order = Order(UUID.randomUUID(), Location.random())
        order.assignCourier(Courier("курьер1", "транспорт1", 1, Location.random()))
        whenever(orderRepository.allAssigned())
            .thenReturn(listOf(order))

        whenever(courierRepository.findBy(any()))
            .thenReturn(null)

        assertThrows<RuntimeException> { moveCourierHandler.handle(MoveCourierCommand()) }

        verify(orderRepository, never()).update(any())
        verify(courierRepository, never()).update(any())
    }

    @Test
    fun `move courier if one ASSIGN orders and courier exists`() {
        val courierLocation = Location(5, 5)

        val order = Order(UUID.randomUUID(), Location(1, 1))
        val courier = Courier("курьер1", "транспорт1", 1, courierLocation)
        order.assignCourier(courier)
        courier.busy()

        whenever(orderRepository.allAssigned())
            .thenReturn(listOf(order))

        whenever(courierRepository.findBy(any()))
            .thenReturn(courier)

        val courierCaptor = argumentCaptor<Courier>()
        whenever(courierRepository.update(courierCaptor.capture()))
            .thenAnswer { it.arguments[0] }

        moveCourierHandler.handle(MoveCourierCommand())

        verify(orderRepository).update(any())
        verify(courierRepository).update(any())

        assertFalse(courierLocation == courierCaptor.firstValue.location)
    }

    @Test
    fun `move courier and order is COMPLETED if one ASSIGN orders and courier exists`() {
        val bothLocation = Location(5, 5)

        val order = Order(UUID.randomUUID(), bothLocation)
        val courier = Courier("курьер1", "транспорт1", 1, bothLocation)
        order.assignCourier(courier)
        courier.busy()

        whenever(orderRepository.allAssigned())
            .thenReturn(listOf(order))
        whenever(courierRepository.findBy(any()))
            .thenReturn(courier)

        val orderCapture = argumentCaptor<Order>()
        whenever(orderRepository.update(orderCapture.capture()))
            .thenAnswer { it.arguments[0] }
        val courierCaptor = argumentCaptor<Courier>()
        whenever(courierRepository.update(courierCaptor.capture()))
            .thenAnswer { it.arguments[0] }

        moveCourierHandler.handle(MoveCourierCommand())

        verify(orderRepository).update(any())
        verify(courierRepository).update(any())

        assertEquals(OrderStatus.COMPLETED, orderCapture.firstValue.status)
        assertEquals(CourierStatus.FREE, courierCaptor.firstValue.status)
    }
}