package com.example.core.base

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Activity với BaseViewModel, đã cài đặt sẵn xử lý loading và error
 */
abstract class ViewModelActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() {

    /**
     * ViewModel của activity
     */
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBaseObservers()
    }

    /**
     * Cài đặt observers cơ bản cho loading và errors
     */
    private fun setupBaseObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collectLatest { isLoading ->
                    handleLoading(isLoading)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { errorMessage ->
                    handleError(errorMessage)
                }
            }
        }
    }

    /**
     * Xử lý khi có loading, override lại trong class con để hiển thị UI loading
     * @param isLoading Trạng thái loading
     */
    open fun handleLoading(isLoading: Boolean) {
        // Override trong class con để hiển thị UI loading
    }

    /**
     * Xử lý khi có lỗi xảy ra, override lại trong class con để hiển thị UI error
     * @param message Thông báo lỗi
     */
    open fun handleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
} 