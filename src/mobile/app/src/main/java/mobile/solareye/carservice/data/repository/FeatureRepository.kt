package mobile.solareye.carservice.data.repository

import mobile.solareye.carservice.data.model.Order
import mobile.solareye.carservice.data.model.Result

interface FeatureRepository {

    suspend fun getOrders(
        master: String?,
        deviceId: String,
    ): Result<List<Order>>

    suspend fun getOrder(orderId: Int): Result<Order>

    suspend fun createOrder(
        carName: String,
        carLicensePlate: String,
        cost: String,
        master: String,
        comment: String,
        deviceId: String,
    ): Result<Unit>

    suspend fun closeOrder(
        orderId: Int,
        cost: String,
    ): Result<Unit>

    suspend fun reopenOrder(
        orderId: Int,
    ): Result<Unit>

    suspend fun updateOrder(
        orderId: String,
        carName: String,
        carLicensePlate: String,
        cost: String,
        master: String,
        comment: String,
        deviceId: String,
    ): Result<Unit>

}