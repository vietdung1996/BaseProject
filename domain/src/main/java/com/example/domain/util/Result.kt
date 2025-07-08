package com.example.domain.util

/**
 * A Result type that encapsulates successful outcomes and domain errors
 * Following Clean Architecture principles for better error handling
 */
sealed class Result<out T> {
    /**
     * Represents successful execution with data
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents failed execution with domain error
     */
    data class Error(val error: DomainError) : Result<Nothing>()
    
    /**
     * Represents loading state
     */
    object Loading : Result<Nothing>()

    /**
     * Returns true if the result is successful
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Returns true if the result is an error
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Returns true if the result is loading
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * Returns the data if successful, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Returns the error if failed, null otherwise
     */
    fun errorOrNull(): DomainError? = when (this) {
        is Error -> error
        else -> null
    }

    /**
     * Maps the success value to another type
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> this
    }

    /**
     * Flat maps the success value to another Result
     */
    inline fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(data)
        is Error -> this
        is Loading -> this
    }

    /**
     * Performs an action if the result is successful
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    /**
     * Performs an action if the result is an error
     */
    inline fun onError(action: (DomainError) -> Unit): Result<T> {
        if (this is Error) action(error)
        return this
    }

    /**
     * Performs an action if the result is loading
     */
    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) action()
        return this
    }
}

// Extension functions for Result companion object
companion object {
    /**
     * Creates a successful Result
     */
    fun <T> success(data: T): Result<T> = Success(data)

    /**
     * Creates an error Result
     */
    fun error(error: DomainError): Result<Nothing> = Error(error)

    /**
     * Creates a loading Result
     */
    fun loading(): Result<Nothing> = Loading
}