package com.example.baseproject.ui.viewmodel

/**
 * Generic state representation for UI responses
 * @param T The type of data to be wrapped in the response state
 */
sealed class ResponseState<out T> {
    object Loading : ResponseState<Nothing>()
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Error(val message: String) : ResponseState<Nothing>()

    /**
     * Helper function to handle each state
     */
    fun <R> fold(
        onLoading: () -> R,
        onSuccess: (T) -> R,
        onError: (String) -> R
    ): R = when (this) {
        is Loading -> onLoading()
        is Success -> onSuccess(data)
        is Error -> onError(message)
    }
} 