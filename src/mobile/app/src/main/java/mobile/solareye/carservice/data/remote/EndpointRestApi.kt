package mobile.solareye.carservice.data.remote

import mobile.solareye.carservice.data.model.*
import retrofit2.http.*

interface EndpointRestApi {

    @GET("orders")
    suspend fun getOrders(
        @Query("master") master: String?,
        @Query("deviceId") deviceId: String,
    ): List<OrderDto>

    @GET("orders/{orderId}")
    suspend fun getOrder(
        @Path("orderId") orderId: Int,
    ): OrderDto

    @FormUrlEncoded
    @POST("orders")
    suspend fun createOrder(
        @Field("carName") carName: String,
        @Field("carLicensePlate") carLicensePlate: String,
        @Field("cost") cost: String,
        @Field("comment") comment: String,
        @Field("master") master: String,
        @Field("deviceId") deviceId: String,
    )

    @FormUrlEncoded
    @POST("orderclose/{orderId}")
    suspend fun closeOrder(
        @Path("orderId") orderId: Int,
        @Field("cost") cost: String,
    )

    @POST("orderreopen/{orderId}")
    suspend fun reopenOrder(
        @Path("orderId") orderId: Int,
    )

    @FormUrlEncoded
    @POST("orders/{orderId}")
    suspend fun updateOrder(
        @Path("orderId") orderId: String,
        @Field("carName") carName: String,
        @Field("carLicensePlate") carLicensePlate: String,
        @Field("cost") cost: String,
        @Field("comment") comment: String,
        @Field("master") master: String,
        @Field("deviceId") deviceId: String,
    )

}