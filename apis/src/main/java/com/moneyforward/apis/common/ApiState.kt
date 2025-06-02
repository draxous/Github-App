package com.moneyforward.apis.common

/**
 * Represents the state of an API call.
 *
 * @param T The type of data returned by the API call.
 * @property isLoading True if the API call is in progress, false otherwise.
 * @property result The result of the API call, or null if the call is in progress or has not yet been made.
 */
data class ApiState<out T>(
    val isLoading: Boolean = false,
    val result: ApiResult<T>? = null
) {
    companion object {
        /**
         * Creates an [ApiState] representing a loading state.
         *
         * @param T The type of data that will be returned by the API call.
         */
        fun <T> loading() = ApiState<T>(isLoading = true)
        /**
         * Creates an [ApiState] representing a successful API call.
         *
         * @param T The type of data returned by the API call.
         * @param data The data returned by the API call.
         */
        fun <T> success(data: T) = ApiState(isLoading = false, result = ApiResult.Success(data))
        /**
         * Creates an [ApiState] representing an API call that resulted in an error.
         *
         * @param throwable The error that occurred.
         */
        fun <T> error(throwable: Throwable) = ApiState<T>(isLoading = false, result = ApiResult.Error(throwable))
        /**
         * Creates an [ApiState] representing an API call that resulted in a network error.
         */
        fun <T> networkError(throwable: Throwable) = ApiState<T>(isLoading = false, result = ApiResult.NetworkError(throwable))
    }
}