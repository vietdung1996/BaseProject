package com.example.domain.util

/**
 * Domain layer error types following Clean Architecture principles
 * These represent business-level errors that the domain layer can understand
 */
sealed class DomainError(
    val message: String,
    val cause: Throwable? = null
) {
    /**
     * Network connectivity issues
     */
    data class NetworkError(
        val errorMessage: String = "Network connection error",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)

    /**
     * Server-side errors
     */
    data class ServerError(
        val errorMessage: String = "Server error occurred",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)

    /**
     * Authentication/authorization errors
     */
    data class AuthenticationError(
        val errorMessage: String = "Authentication failed",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)

    /**
     * Resource not found errors
     */
    data class NotFoundError(
        val errorMessage: String = "Requested resource not found",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)

    /**
     * Business logic validation errors
     */
    data class ValidationError(
        val errorMessage: String = "Validation failed",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)

    /**
     * Local storage/cache errors
     */
    data class LocalStorageError(
        val errorMessage: String = "Local storage error",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)

    /**
     * Unknown or unclassified errors
     */
    data class UnknownError(
        val errorMessage: String = "An unknown error occurred",
        val throwable: Throwable? = null
    ) : DomainError(errorMessage, throwable)
}