package mobile.solareye.carservice.data.model

data class Order(
    val orderId: Int,
    val createdGmt: String,
    val closedGmt: String?,
    val status: String,
    val carName: String,
    val carLicensePlate: String,
    val cost: String,
    val comment: String,
    val master: String,
)