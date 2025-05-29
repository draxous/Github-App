package com.moneyforward.api.common

data class ApiState<out T>(
    val isLoading: Boolean = false,
    val result: ApiResult<T>? = null
) {
    companion object {
        fun <T> loading() = ApiState<T>(isLoading = true)
        fun <T> success(data: T) = ApiState(isLoading = false, result = ApiResult.Success(data))
        fun <T> error(throwable: Throwable) = ApiState<T>(isLoading = false, result = ApiResult.Error(throwable))
        fun <T> networkError(throwable: Throwable) = ApiState<T>(isLoading = false, result = ApiResult.NetworkError(throwable))
    }
}