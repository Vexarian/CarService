package mobile.solareye.carservice.data.remote

import android.content.Context
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import mobile.solareye.carservice.data.model.*
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Path
import retrofit2.http.Query

class MockRestApi(
    private val context: Context
) : EndpointRestApi {

    private companion object {
        const val DELAY = 500L
    }

    override suspend fun getOrders(
        master: String?,
        deviceId: String,
    ): List<OrderDto> {
        delay(DELAY)
        return MockUtils.readEntityFromJson(
            context,
            "orders.json",
            object : TypeToken<List<OrderDto>>() {}.type
        )
    }

    override suspend fun getOrder(orderId: Int): OrderDto {
        delay(DELAY)
        return MockUtils.readEntityFromJson(
            context,
            "order.json",
            object : TypeToken<OrderDto>() {}.type
        )
    }

    override suspend fun createOrder(
        carName: String,
        carLicensePlate: String,
        cost: String,
        comment: String,
        master: String,
        deviceId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun closeOrder(
        orderId: Int,
        cost: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun reopenOrder(orderId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(
        orderId: String,
        carName: String,
        carLicensePlate: String,
        cost: String,
        comment: String,
        master: String,
        deviceId: String,
    ) {
        TODO("Not yet implemented")
    }

}