package com.example.data.mapper

import com.example.data.common.exception.DataException
import com.example.domain.util.DomainError

/**
 * Maps data layer exceptions to domain errors
 * This maintains the separation between layers while providing meaningful error information
 */
object ErrorMapper {
    
    /**
     * Converts DataException to DomainError
     */
    fun mapToDomainError(dataException: DataException): DomainError {
        return when (dataException) {
            is DataException.Network -> DomainError.NetworkError(
                errorMessage = dataException.message,
                throwable = dataException.cause
            )
            
            is DataException.Timeout -> DomainError.NetworkError(
                errorMessage = "Request timeout: ${dataException.message}",
                throwable = dataException.cause
            )
            
            is DataException.Unauthorized -> DomainError.AuthenticationError(
                errorMessage = dataException.message,
                throwable = dataException.cause
            )
            
            is DataException.Forbidden -> DomainError.AuthenticationError(
                errorMessage = "Access forbidden: ${dataException.message}",
                throwable = dataException.cause
            )
            
            is DataException.NotFound -> DomainError.NotFoundError(
                errorMessage = dataException.message,
                throwable = dataException.cause
            )
            
            is DataException.Server -> DomainError.ServerError(
                errorMessage = dataException.message,
                throwable = dataException.cause
            )
            
            is DataException.Database -> DomainError.LocalStorageError(
                errorMessage = dataException.message,
                throwable = dataException.cause
            )
            
            is DataException.Parse -> DomainError.UnknownError(
                errorMessage = "Data parsing error: ${dataException.message}",
                throwable = dataException.cause
            )
            
            is DataException.Unknown -> DomainError.UnknownError(
                errorMessage = dataException.message,
                throwable = dataException.cause
            )
        }
    }
    
    /**
     * Converts any Throwable to DomainError with basic classification
     */
    fun mapToDomainError(throwable: Throwable): DomainError {
        return when (throwable) {
            is DataException -> mapToDomainError(throwable)
            
            // Network-related exceptions
            is java.net.UnknownHostException,
            is java.net.ConnectException,
            is java.net.SocketTimeoutException -> DomainError.NetworkError(
                errorMessage = throwable.message ?: "Network connection failed",
                throwable = throwable
            )
            
            // Security-related exceptions
            is SecurityException -> DomainError.AuthenticationError(
                errorMessage = throwable.message ?: "Security error",
                throwable = throwable
            )
            
            // Generic fallback
            else -> DomainError.UnknownError(
                errorMessage = throwable.message ?: "An unexpected error occurred",
                throwable = throwable
            )
        }
    }
}