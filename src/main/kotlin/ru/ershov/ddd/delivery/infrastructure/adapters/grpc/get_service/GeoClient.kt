package ru.ershov.ddd.delivery.infrastructure.adapters.grpc.get_service

import geo.GeoContract
import geo.GeoGrpc
import org.springframework.stereotype.Service
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.core.ports.IGeoClient

@Service
class GeoClient(
    private val geoStub: GeoGrpc.GeoBlockingStub,
) : IGeoClient {

    override fun getBy(address: String): Location {
        println(geoStub.channel.toString())
        return geoStub.getGeolocation(
            GeoContract.GetGeolocationRequest.newBuilder()
                .setStreet(address)
                .build()
        ).let { Location(it.location.x, it.location.y) }
    }

}