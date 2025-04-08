package ru.ershov.ddd.delivery.infrastructure.adapters.postgres

import org.springframework.stereotype.Component
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import ru.ershov.ddd.delivery.core.ports.ICourierRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Component
internal class CourierRepository(
    private val courierJpaRepository: CourierJpaRepository
): ICourierRepository {
    override fun add(courier: Courier): Courier =
        courierJpaRepository.save(courier)

    override fun update(courier: Courier): Courier =
        courierJpaRepository.save(courier)

    override fun findBy(id: UUID): Courier? =
        courierJpaRepository.findById(id).getOrNull()

    override fun findAllFree(): List<Courier> =
        courierJpaRepository.findByStatusIs(CourierStatus.FREE)
}