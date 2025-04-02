package ru.ershov.ddd.delivery.infrastructure.adapters.postgres

import org.springframework.data.jpa.repository.JpaRepository
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.CourierStatus
import java.util.UUID


interface CourierJpaRepository: JpaRepository<Courier, UUID> {

    fun findByStatusIs(status: CourierStatus): List<Courier>
}