package mobile.solareye.carservice.data.model


sealed class Result<out T> {

    class Success<T>(
        val data: T
    ) : Result<T>()

    class Failure<T>(
        val error: ErrorEntity
    ) : Result<T>()
}

inline fun <T> handleResult(
    networkCall: () -> T,
    errorMapper: (Throwable) -> ErrorEntity
): Result<T> {
    return try {
        Result.Success(networkCall.invoke())
    } catch (ex: Throwable) {
        Result.Failure(errorMapper.invoke(ex))
    }
}

fun <T> Result<T>.onSuccess(block: (Result.Success<T>) -> Unit): Result<T> {
    if (this is Result.Success) {
        block.invoke(this)
    }
    return this
}

fun <T> Result<T>.onFailure(block: (Result.Failure<T>) -> Unit): Result<T> {
    if (this is Result.Failure && this.error !is CancelError) {
        block.invoke(this)
    }
    return this
}