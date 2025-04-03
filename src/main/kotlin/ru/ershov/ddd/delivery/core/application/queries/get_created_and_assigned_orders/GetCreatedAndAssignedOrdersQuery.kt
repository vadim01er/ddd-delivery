package ru.ershov.ddd.delivery.core.application.queries.get_created_and_assigned_orders

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ershov.ddd.delivery.core.domain.model.order_aggregate.OrderStatus
import java.util.*

@Service
@Transactional
class GetCreatedAndAssignedOrdersQuery(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun query(getCreatedAndAssignedOrdersRequest: GetCreatedAndAssignedOrdersRequest): GetCreatedAndAssignedOrdersResponse {
        return jdbcTemplate.query(
            "select id, x, y from orders where status in (:statuses)",
            MapSqlParameterSource("statuses", listOf(OrderStatus.CREATED.name, OrderStatus.ASSIGNED.name))
        ) { rs, _ ->
            val location = GetCreatedAndAssignedOrdersResponse.Order.Location(
                x = rs.getInt("x"),
                y = rs.getInt("y"),
            )
            val order = GetCreatedAndAssignedOrdersResponse.Order(
                id = UUID.fromString(rs.getString("id")),
                location = location,
            )
            order
        }.let { GetCreatedAndAssignedOrdersResponse(it) }
            .also { println(it) }
    }

}