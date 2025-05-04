package com.example.core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Base activity class with common functionality
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: error("Binding is only valid between onCreate and onDestroy.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding()
        setContentView(binding.root)

        initView()
        initObservers()
    }

    /**
     * Inflate ViewBinding using reflection
     */
    @Suppress("UNCHECKED_CAST")
    private fun inflateBinding(): VB {
        var type = javaClass.genericSuperclass
        var clazz: Class<*>? = null

        // Traverse until find BaseActivity
        while (type != null && type !is ParameterizedType) {
            type = (type as? Class<*>)?.genericSuperclass
        }
        if (type is ParameterizedType) {
            clazz = type.actualTypeArguments[0] as? Class<*>
        }

        requireNotNull(clazz) { "ViewBinding class not found." }
        val inflateMethod = clazz.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, layoutInflater) as VB
    }

    /**
     * Override để khởi tạo view
     */
    open fun initView() {}

    /**
     * Override để khởi tạo observer
     */
    open fun initObservers() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
} 