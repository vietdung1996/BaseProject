package com.example.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.example.core.R

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val button: AppCompatButton
    private val progressBar: ProgressBar

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_loading_button, this, true)
        button = root.findViewById(R.id.button)
        progressBar = root.findViewById(R.id.progress_bar)
    }

    fun showLoading(show: Boolean) {
        button.isEnabled = !show
        progressBar.isVisible = show
    }

    fun setText(text: String) {
        button.text = text
    }

    fun setButtonClickListener(listener: View.OnClickListener) {
        button.setOnClickListener(listener)
    }
} 