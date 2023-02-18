package mobile.solareye.carservice.data

import android.util.Log
import mobile.solareye.carservice.data.model.CancelError
import mobile.solareye.carservice.data.model.ErrorEntity
import mobile.solareye.carservice.data.model.NotFoundError
import mobile.solareye.carservice.data.model.UnknownError
import retrofit2.HttpException
import java.util.concurrent.CancellationException

const val CAR_SERVICE_REMOTE_TAG = "CAR_SERVICE_REMOTE_TAG"

open class BaseErrorMapper {

    open fun map(from: Throwable): ErrorEntity = when (from) {
        is HttpException -> mapHttpException(from)
        is CancellationException -> CancelError
        else                     -> UnknownError
    }.also {
        Log.d(CAR_SERVICE_REMOTE_TAG, "ErrorMapper: error ${from.message}")
    }

    open fun mapHttpException(e: HttpException) = when (e.code()) {
        400  -> NotFoundError
        else -> UnknownError
    }

}