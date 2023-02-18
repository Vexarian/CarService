package mobile.solareye.carservice.data.model

open class ErrorEntity : Throwable()

object NotFoundError : ErrorEntity()

object UnknownError : ErrorEntity()

object CancelError : ErrorEntity()