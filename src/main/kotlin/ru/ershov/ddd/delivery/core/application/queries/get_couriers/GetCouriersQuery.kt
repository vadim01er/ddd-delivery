package ru.ershov.ddd.delivery.core.application.queries.get_couriers

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import ru.ershov.ddd.delivery.infrastructure.adapters.postgres.CourierJpaRepository

@Service
@Transactional
class GetCouriersQuery(
    private val courierRepository: CourierJpaRepository
) {

    fun query(getCouriersRequest: GetCouriersRequest): GetCouriersResponse {
        return courierRepository.findByStatusIs(CourierStatus.BUSY)
            .map { it.toResponseCourier() }
            .let { GetCouriersResponse(it) }
    }

    private fun Courier.toResponseCourier() =
        GetCouriersResponse.Courier(
            id = this.id,
            name = this.name,
            location = GetCouriersResponse.Courier.Location(this.location.x, this.location.y)
        )
}