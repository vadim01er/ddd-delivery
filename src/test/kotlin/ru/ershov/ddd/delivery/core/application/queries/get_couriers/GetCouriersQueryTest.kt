package ru.ershov.ddd.delivery.core.application.queries.get_couriers

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import ru.ershov.ddd.delivery.infrastructure.adapters.postgres.CourierJpaRepository

@SpringBootTest
@ActiveProfiles("dev")
class GetCouriersQueryTest {

    @Autowired
    lateinit var courierRepository: CourierJpaRepository

    @Autowired
    lateinit var getCouriersQuery: GetCouriersQuery

    @BeforeEach
    fun setUp() {
        courierRepository.deleteAll()
    }

    @Test
    fun `find empty busy courier`() {

        val query = getCouriersQuery.query(GetCouriersRequest())

        assertEquals(0, query.couriers.size)
    }

    @Test
    fun `find one busy courier`() {
        val courier = Courier("курьер1", "транспорт1", 1, Location.random())
        courier.busy()
        courierRepository.save(courier)

        val query = getCouriersQuery.query(GetCouriersRequest())

        assertEquals(1, query.couriers.size)
        assertEquals(courier.id, query.couriers[0].id)

    }
}