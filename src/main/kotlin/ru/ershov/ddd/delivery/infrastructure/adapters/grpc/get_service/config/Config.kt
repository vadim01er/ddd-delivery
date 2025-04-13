package ru.ershov.ddd.delivery.infrastructure.adapters.grpc.get_service.config

import geo.GeoGrpc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.grpc.client.GrpcChannelFactory


@Configuration
class Config {

    @Bean
    fun geoStub(channels: GrpcChannelFactory): GeoGrpc.GeoBlockingStub {
        return GeoGrpc.newBlockingStub(channels.createChannel("geo"))
    }
}