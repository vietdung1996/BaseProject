package com.example.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel với hỗ trợ xử lý lỗi và hiển thị loading
 */
abstract class BaseViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    /**
     * Thực hiện công việc với try-catch và tự động xử lý loading/error
     * @param dispatcher CoroutineDispatcher sử dụng cho công việc
     * @param showLoading Có hiển thị loading khi thực hiện không
     * @param block Khối lệnh cần thực hiện
     */
    protected fun executeTask(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        showLoading: Boolean = true,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                if (showLoading) _loading.value = true
                block()
            } catch (e: Exception) {
                _error.emit(e.message ?: "Unknown error")
            } finally {
                if (showLoading) _loading.value = false
            }
        }
    }

    /**
     * Thực hiện công việc với try-catch và tự động xử lý loading/error, có kết quả trả về
     * @param dispatcher CoroutineDispatcher sử dụng cho công việc
     * @param showLoading Có hiển thị loading khi thực hiện không
     * @param onSuccess Callback khi thành công, với kết quả trả về
     * @param block Khối lệnh cần thực hiện, trả về kết quả
     */
    protected fun <T> executeTaskWithResult(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        showLoading: Boolean = true,
        onSuccess: suspend (T) -> Unit,
        block: suspend () -> T
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                if (showLoading) _loading.value = true
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                _error.emit(e.message ?: "Unknown error")
            } finally {
                if (showLoading) _loading.value = false
            }
        }
    }
} 