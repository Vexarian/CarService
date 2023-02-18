package mobile.solareye.carservice.data.model

data class OrderDto(
    val orderId: String,
    val createdGmt: String,
    val closedGmt: String?,
    val orderStatus: String,
    val carName: String,
    val carLicensePlate: String,
    val cost: String,
    val orderComment: String,
    val masterName: String,
    val deviceId: String,
)