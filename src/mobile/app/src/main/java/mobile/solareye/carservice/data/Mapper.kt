package mobile.solareye.carservice.data

import mobile.solareye.carservice.data.model.Order
import mobile.solareye.carservice.data.model.OrderDto

object Mapper {

    fun List<OrderDto>.toOrders() = map { it.toOrder() }

    fun OrderDto.toOrder() = Order(
            orderId = orderId.toInt(),
            createdGmt = createdGmt,
            closedGmt = closedGmt,
            status = orderStatus,
            carName = carName,
            carLicensePlate = carLicensePlate,
            cost = cost,
            comment = orderComment,
            master = masterName,
        )

}