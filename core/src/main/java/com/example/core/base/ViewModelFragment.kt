package com.example.core.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment với BaseViewModel, đã cài đặt sẵn xử lý loading và error
 */
abstract class ViewModelFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB, VM>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBaseObservers()
    }

    /**
     * Cài đặt observers cơ bản cho loading và errors
     */
    private fun setupBaseObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collectLatest { isLoading ->
                    handleLoading(isLoading)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
} 