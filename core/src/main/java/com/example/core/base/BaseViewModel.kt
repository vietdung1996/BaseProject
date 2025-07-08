package com.example.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.state.UiState
import com.example.core.mapper.UiStateMapper.toUiState
import com.example.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Enhanced Base ViewModel with improved state management and error handling
 * Uses UiState for consistent state representation across the app
 */
abstract class BaseViewModel : ViewModel() {

    // Removed individual loading and error flows in favor of UiState pattern
    // Child classes should manage their own state using UiState

    /**
     * Executes a domain operation and maps the result to UiState
     * @param dispatcher CoroutineDispatcher for the operation
     * @param stateFlow MutableStateFlow to update with the result
     * @param operation Domain operation that returns a Result
     */
    protected fun <T> executeOperation(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        stateFlow: MutableStateFlow<UiState<T>>,
        operation: suspend () -> Result<T>
    ) {
        viewModelScope.launch(dispatcher) {
            stateFlow.value = UiState.Loading
            try {
                val result = operation()
                stateFlow.value = result.toUiState()
            } catch (e: Exception) {
                stateFlow.value = UiState.Error(
                    message = e.message ?: "An unexpected error occurred",
                    throwable = e
                )
            }
        }
    }

    /**
     * Executes a domain operation with callback-based result handling
     * @param dispatcher CoroutineDispatcher for the operation
     * @param onResult Callback for handling the result
     * @param operation Domain operation that returns a Result
     */
    protected fun <T> executeOperationWithCallback(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (UiState<T>) -> Unit,
        operation: suspend () -> Result<T>
    ) {
        viewModelScope.launch(dispatcher) {
            onResult(UiState.Loading)
            try {
                val result = operation()
                onResult(result.toUiState())
            } catch (e: Exception) {
                onResult(UiState.Error(
                    message = e.message ?: "An unexpected error occurred",
                    throwable = e
                ))
            }
        }
    }

    /**
     * Legacy method for backward compatibility - deprecated
     * Use executeOperation instead
     */
    @Deprecated(
        message = "Use executeOperation with UiState instead",
        replaceWith = ReplaceWith("executeOperation")
    )
    protected fun executeTask(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        showLoading: Boolean = true,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                block()
            } catch (e: Exception) {
                // Legacy error handling - no longer used
            }
        }
    }

    /**
     * Legacy method for backward compatibility - deprecated
     * Use executeOperation instead
     */
    @Deprecated(
        message = "Use executeOperation with UiState instead",
        replaceWith = ReplaceWith("executeOperation")
    )
    protected fun <T> executeTaskWithResult(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        showLoading: Boolean = true,
        onSuccess: suspend (T) -> Unit,
        block: suspend () -> T
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                // Legacy error handling - no longer used
            }
        }
    }
} 