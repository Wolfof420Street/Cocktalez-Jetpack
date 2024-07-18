package com.wolf.cocktalezandroid.common.util

sealed class AppState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : AppState<T>(data)
    class Loading<T>(data: T? = null) : AppState<T>(data)
    class Error<T>(message: String, data: T? = null) : AppState<T>(data, message)
}