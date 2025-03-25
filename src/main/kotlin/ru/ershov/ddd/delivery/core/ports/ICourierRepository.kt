package ru.ershov.ddd.delivery.core.ports

import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import java.util.UUID

interface ICourierRepository {

    fun add(courier: Courier): Courier

    fun update(courier: Courier): Courier

    fun findBy(id: UUID): Courier?

    fun findAllFree(): List<Courier>
}