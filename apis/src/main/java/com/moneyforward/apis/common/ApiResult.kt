package com.moneyforward.apis.common

/**
 * Class representing the result of an API call.
 *
 * [ApiResult] can be one of three types:
 * [ApiResult.Success], [ApiResult.Error], or [ApiResult.NetworkError].
 *
 * @param T The type of the data returned by the API call.
 */
sealed class ApiResult<out T> {

    /**
     * Class representing a successful API call.
     *
     * @param data The data returned by the API call.
     */
    data class Success<out T>(val data: T) : ApiResult<T>()

    /**
     * Class representing an unsuccessful API call.
     *
     * @param throwable The [Throwable] that caused the API call to fail.
     */
    data class Error(val throwable: Throwable) : ApiResult<Nothing>()

    /**
     * Class representing a network error that occurred during an API call.
     *
     * @param throwable The [Throwable] that caused the network error.
     */
    data class NetworkError(val throwable: Throwable) : ApiResult<Nothing>()

    /**
     * Returns `true` if this [ApiResult] is [ApiResult.Success], `false` otherwise.
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Returns `true` if this [ApiResult] is [ApiResult.Error], `false` otherwise.
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Returns `true` if this [ApiResult] is [ApiResult.NetworkError], `false` otherwise.
     */
    val isNetworkError: Boolean
        get() = this is NetworkError

    /**
     * Returns the data from this [ApiResult] if it is [ApiResult.Success], or `null` otherwise.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
}