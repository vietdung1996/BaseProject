package com.example.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * Base Fragment với ViewBinding
 * @param VB: Type của ViewBinding
 * @param VM: Type của ViewModel
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView.")

    /**
     * ViewModel của fragment
     */
    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    /**
     * Inflate ViewBinding using reflection
     */
    @Suppress("UNCHECKED_CAST")
    private fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        var type = javaClass.genericSuperclass
        var clazz: Class<*>? = null

        // Traverse until find BaseFragment with proper parameterized type
        while (type != null && type !is ParameterizedType) {
            type = (type as? Class<*>)?.genericSuperclass
        }
        if (type is ParameterizedType) {
            clazz = type.actualTypeArguments[0] as? Class<*>
        }

        requireNotNull(clazz) { "ViewBinding class not found." }
        val inflateMethod = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    /**
     * Khởi tạo view, override lại trong class con
     */
    open fun initView() {}

    /**
     * Khởi tạo observers, override lại trong class con
     */
    open fun initObservers() {}

    /**
     * Helper function để collect Flow khi fragment đang ở trạng thái STARTED
     */
    protected fun <T> collectFlow(
        collect: suspend () -> T,
        onCollect: (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect().let { onCollect(it) }
            }
        }
    }

    /**
     * Helper function để collect Flow liên tục khi fragment đang ở trạng thái STARTED
     */
    protected fun <T> collectFlowWithLifecycle(
        collect: suspend () -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 