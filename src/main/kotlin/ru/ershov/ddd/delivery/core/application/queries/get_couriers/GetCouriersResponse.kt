package ru.ershov.ddd.delivery.core.application.queries.get_couriers

import java.util.UUID

class GetCouriersResponse(
    val couriers: List<Courier>,
) {


    class Courier(
        val id: UUID,
        val name: String,
        val location: Location
    ) {

        class Location(
            val x: Int,
            val y: Int,
        )
    }
}