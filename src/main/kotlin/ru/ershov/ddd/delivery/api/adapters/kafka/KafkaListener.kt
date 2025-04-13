package ru.ershov.ddd.delivery.api.adapters.kafka

import BasketConfirmed.CreateOrder
import BasketConfirmed.CreateOrder.BasketConfirmedIntegrationEvent.Builder
import com.google.protobuf.util.JsonFormat
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.ershov.ddd.delivery.core.application.commands.create_order.CreateOrderCommand
import ru.ershov.ddd.delivery.core.application.commands.create_order.CreateOrderHandler
import java.util.*


@Service
class KafkaListener(
    private val createOrderHandler: CreateOrderHandler,
) {

    @KafkaListener(
        topics = ["basket.confirmed"],
        groupId = "delivery-service",
    )
    fun receivedMessage(createOrderMessage: String) {
        val builder: Builder = CreateOrder.BasketConfirmedIntegrationEvent.newBuilder()
        JsonFormat.parser().merge(createOrderMessage, builder)
        val createOrder = builder.build()

        createOrderHandler.handle(CreateOrderCommand(UUID.fromString(createOrder.basketId), createOrder.address.street))
    }
}
