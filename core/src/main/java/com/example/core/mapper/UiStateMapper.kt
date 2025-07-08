package com.example.core.mapper

import com.example.core.state.UiState
import com.example.domain.util.Result
import com.example.domain.util.DomainError

/**
 * Maps domain layer Results to UI layer states
 * Provides translation between domain and presentation layers
 */
object UiStateMapper {
    
    /**
     * Converts a domain Result to UiState
     */
    fun <T> Result<T>.toUiState(): UiState<T> {
        return when (this) {
            is Result.Success -> {
                // Handle empty collections/lists
                val uiData = data
                if (uiData is Collection<*> && uiData.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(uiData)
                }
            }
            is Result.Error -> UiState.Error(
                message = mapErrorToUserMessage(error),
                throwable = error.cause
            )
            is Result.Loading -> UiState.Loading
        }
    }
    
    /**
     * Maps domain errors to user-friendly messages
     */
    private fun mapErrorToUserMessage(error: DomainError): String {
        return when (error) {
            is DomainError.NetworkError -> "Please check your internet connection and try again"
            is DomainError.ServerError -> "Server is temporarily unavailable. Please try again later"
            is DomainError.AuthenticationError -> "Please check your credentials and try again"
            is DomainError.NotFoundError -> "The requested information was not found"
            is DomainError.ValidationError -> error.message
            is DomainError.LocalStorageError -> "There was a problem accessing local data"
            is DomainError.UnknownError -> "Something went wrong. Please try again"
        }
    }
}