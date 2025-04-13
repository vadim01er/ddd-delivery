package ru.ershov.ddd.delivery.core.ports

import ru.ershov.ddd.delivery.core.domain.shared.kernel.Location


interface IGeoClient {

    fun getBy(address: String) : Location
}