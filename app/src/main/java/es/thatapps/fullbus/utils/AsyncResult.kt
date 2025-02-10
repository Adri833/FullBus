package es.thatapps.fullbus.utils

sealed class AsyncResult<out T> {
    object Loading : AsyncResult<Nothing>()
    data class Success<out T>(val data: T) : AsyncResult<T>()
    data class Error(
        val message: Any, // Permite Int y String
        val throwable: Throwable? = null
    ) : AsyncResult<Nothing>()
    object Idle : AsyncResult<Nothing>()
}

fun AsyncResult<*>.isLoading() = this is AsyncResult.Loading

fun AsyncResult<*>.isError() = this is AsyncResult.Error

fun AsyncResult<*>.isSuccess() = this is AsyncResult.Success

fun setLoading(): AsyncResult.Loading = AsyncResult.Loading