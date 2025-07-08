package com.example.core.state

/**
 * Unified UI state representation for consistent state management
 * Separates loading, content, and error states clearly
 */
sealed class UiState<out T> {
    /**
     * Initial state before any action
     */
    object Idle : UiState<Nothing>()
    
    /**
     * Loading state
     */
    object Loading : UiState<Nothing>()
    
    /**
     * Success state with data
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * Error state with message
     */
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>()
    
    /**
     * Empty state (successful response but no data)
     */
    object Empty : UiState<Nothing>()

    /**
     * Returns true if state is loading
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * Returns true if state is successful
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Returns true if state is error
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Returns true if state is empty
     */
    val isEmpty: Boolean
        get() = this is Empty

    /**
     * Returns true if state is idle
     */
    val isIdle: Boolean
        get() = this is Idle

    /**
     * Returns data if successful, null otherwise
     */
    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Returns error message if error, null otherwise
     */
    fun getErrorOrNull(): String? = when (this) {
        is Error -> message
        else -> null
    }

    /**
     * Maps the success data to another type
     */
    inline fun <R> map(transform: (T) -> R): UiState<R> = when (this) {
        is Success -> Success(transform(data))
        is Loading -> Loading
        is Error -> this
        is Empty -> Empty
        is Idle -> Idle
    }

    /**
     * Performs an action if the state is successful
     */
    inline fun onSuccess(action: (T) -> Unit): UiState<T> {
        if (this is Success) action(data)
        return this
    }

    /**
     * Performs an action if the state is loading
     */
    inline fun onLoading(action: () -> Unit): UiState<T> {
        if (this is Loading) action()
        return this
    }

    /**
     * Performs an action if the state is error
     */
    inline fun onError(action: (String, Throwable?) -> Unit): UiState<T> {
        if (this is Error) action(message, throwable)
        return this
    }

    /**
     * Performs an action if the state is empty
     */
    inline fun onEmpty(action: () -> Unit): UiState<T> {
        if (this is Empty) action()
        return this
    }
}