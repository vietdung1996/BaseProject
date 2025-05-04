package com.example.data.common.network

import com.example.data.common.exception.DataException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles API errors and converts them to appropriate exceptions
 */
@Singleton
class ApiErrorHandler @Inject constructor() {

    /**
     * Processes an exception from a network call and returns a standardized exception
     * @param throwable The original exception from the network call
     * @return A standardized DataException
     */
    fun handleError(throwable: Throwable): DataException {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401 -> DataException.Unauthorized("Authentication error: ${getMessage(throwable)}")
                    403 -> DataException.Forbidden("Access denied: ${getMessage(throwable)}")
                    404 -> DataException.NotFound("Resource not found: ${getMessage(throwable)}")
                    in 500..599 -> DataException.Server("Server error: ${getMessage(throwable)}")
                    else -> DataException.Unknown("API error: ${getMessage(throwable)}")
                }
            }
            is UnknownHostException -> DataException.Network("No internet connection")
            is SocketTimeoutException -> DataException.Timeout("Connection timeout")
            is IOException -> DataException.Network("Network error: ${throwable.message}")
            else -> DataException.Unknown("Unknown error: ${throwable.message}")
        }
    }

    /**
     * Attempts to extract a message from the HttpException response body
     */
    private fun getMessage(exception: HttpException): String {
        return exception.message() ?: exception.message ?: "Unknown error"
    }
} 