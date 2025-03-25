package ru.ershov.ddd.delivery.infrastructure.adapters.postgres

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.ershov.ddd.delivery.core.domain.model.courier_aggregate.Courier
import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location
import java.util.*

@SpringBootTest
@ActiveProfiles("dev")
class CourierRepositoryIntegrationTest {

    @Autowired
    private lateinit var courierRepository: CourierRepository

    @Autowired
    private lateinit var courierJpaRepository: CourierJpaRepository

    @BeforeEach
    fun setup() {
        courierJpaRepository.deleteAll()
    }

    @Test
    fun `add should save and retrieve the courier correctly`() {
        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        val savedCourier = courierRepository.add(courier)

        assertNotNull(savedCourier.id)
        assertEquals(courier.status, savedCourier.status)

        val retrievedCourier = courierRepository.findBy(savedCourier.id)
        assertEquals(savedCourier, retrievedCourier)
    }

    @Test
    fun `update should update and retrieve the courier correctly`() {
        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        val savedCourier = courierRepository.add(courier)

        val updatedCourier = savedCourier
        updatedCourier.busy()

        val updatedSavedCourier = courierRepository.update(updatedCourier)

        assertEquals(updatedCourier.status, updatedSavedCourier.status)

        val retrievedCourier = courierRepository.findBy(updatedSavedCourier.id)
        assertEquals(updatedSavedCourier, retrievedCourier)
    }

    @Test
    fun `findBy should return null if courier is not found`() {
        val id = UUID.randomUUID()
        val retrievedCourier = courierRepository.findBy(id)
        assertNull(retrievedCourier)
    }

    @Test
    fun `findBy should retrieve the courier by ID correctly`() {
        val courier = Courier("Курьер1", "Машины", 3, Location(1, 1))
        val savedCourier = courierRepository.add(courier)

        val retrievedCourier = courierRepository.findBy(savedCourier.id)
        assertEquals(savedCourier, retrievedCourier)
    }

    @Test
    fun `findAllFree should retrieve all couriers with status FREE correctly`() {
        val courier1 = Courier("Курьер1", "Машины", 3, Location(1, 1))
        val courier2 = Courier("Курьер2", "Машины", 3, Location(1, 1))
        val courier3 = Courier("Курьер3", "Машины", 3, Location(1, 1))

        courier2.busy()
        println(courier2)

        courierRepository.add(courier1)
        courierRepository.add(courier2)
        courierRepository.add(courier3)

        val freeCouriers = courierRepository.findAllFree()
        freeCouriers.forEach {
            println(it)
        }
        assertEquals(2, freeCouriers.size)
        assertTrue(freeCouriers.contains(courier1))
        assertTrue(freeCouriers.contains(courier3))
    }
}
