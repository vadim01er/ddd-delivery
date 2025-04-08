package ru.ershov.ddd.delivery.core.application.commands.create_order

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.Order
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.core.ports.IOrderRepository
import java.util.*


@ExtendWith(MockitoExtension::class)
class CreateOrderHandlerTest {

    @Mock
    lateinit var orderRepository: IOrderRepository

    @InjectMocks
    lateinit var handler: CreateOrderHandler

    @Test
    fun `throw exception if exist order by id`() {
        whenever(orderRepository.byId(any()))
            .thenReturn(Order(UUID.randomUUID(), Location.random()))

        val basketId = UUID.randomUUID()
        assertThrows<RuntimeException> { handler.handle(CreateOrderCommand(basketId, "address")) }
    }

    @Test
    fun `created order SUCCESS`() {
        val basketId = UUID.randomUUID()
        handler.handle(CreateOrderCommand(basketId, "address"))

        verify(orderRepository).byId(eq(basketId))
        verify(orderRepository).add(any())
    }
}