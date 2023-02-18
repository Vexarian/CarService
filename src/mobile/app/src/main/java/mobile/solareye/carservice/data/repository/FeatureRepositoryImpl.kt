package mobile.solareye.carservice.data.repository

import mobile.solareye.carservice.data.ErrorMapper
import mobile.solareye.carservice.data.Mapper.toOrder
import mobile.solareye.carservice.data.Mapper.toOrders
import mobile.solareye.carservice.data.model.*
import mobile.solareye.carservice.data.remote.EndpointRestApi

class FeatureRepositoryImpl(
    private val api: EndpointRestApi
) : FeatureRepository {

    override suspend fun getOrders(): Result<List<Order>> {
        return handleResult(
            networkCall = {
                val orders = api.getOrders()
                return@handleResult orders.toOrders()
            },
            errorMapper = ErrorMapper::map
        )
    }

    override suspend fun getOrder(orderId: Int): Result<Order> {
        return handleResult(
            networkCall = {
                val order = api.getOrder(orderId)
                return@handleResult order.toOrder()
            },
            errorMapper = ErrorMapper::map
        )
    }

    override suspend fun createOrder(
        carName: String,
        carLicensePlate: String,
        cost: String,
        master: String,
        comment: String,
        deviceId: String,
    ): Result<Unit> {
        return handleResult(
            networkCall = {
                api.createOrder(
                    carName = carName,
                    carLicensePlate = carLicensePlate,
                    cost = cost,
                    comment = comment,
                    master = master,
                    deviceId = deviceId,
                )
            },
            errorMapper = ErrorMapper::map
        )
    }

    override suspend fun closeOrder(
        orderId: Int,
        cost: String,
    ): Result<Unit> {
        return handleResult(
            networkCall = {
                api.closeOrder(
                    orderId,
                    cost,
                )
            },
            errorMapper = ErrorMapper::map
        )
    }

    override suspend fun reopenOrder(
        orderId: Int,
    ): Result<Unit> {
        return handleResult(
            networkCall = {
                api.reopenOrder(
                    orderId,
                )
            },
            errorMapper = ErrorMapper::map
        )
    }

    override suspend fun updateOrder(
        orderId: String,
        carName: String,
        carLicensePlate: String,
        cost: String,
        master: String,
        comment: String,
        deviceId: String,
    ): Result<Unit> {
        return handleResult(
            networkCall = {
                api.updateOrder(
                    orderId,
                    carName,
                    carLicensePlate,
                    cost,
                    master,
                    comment,
                    deviceId,
                )
            },
            errorMapper = ErrorMapper::map
        )
    }

}