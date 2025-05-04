package com.example.core.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter với DiffUtils
 * @param T: Type của item model
 * @param VH: Type của ViewHolder
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val items = mutableListOf<T>()

    /**
     * Tạo ViewHolder
     */
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * Bind data vào ViewHolder
     */
    abstract override fun onBindViewHolder(holder: VH, position: Int)

    /**
     * Lấy số lượng item
     */
    override fun getItemCount(): Int = items.size

    /**
     * Lấy item tại vị trí position
     */
    fun getItem(position: Int): T = items[position]

    /**
     * Lấy danh sách items hiện tại
     */
    fun getItems(): List<T> = items

    /**
     * Cập nhật toàn bộ danh sách với DiffUtil
     */
    fun submitList(newItems: List<T>) {
        val diffCallback = getDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Thêm item mới
     */
    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    /**
     * Thêm nhiều item mới
     */
    fun addItems(newItems: List<T>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    /**
     * Xóa item tại vị trí cụ thể
     */
    fun removeItemAt(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Xóa item
     */
    fun removeItem(item: T) {
        val position = items.indexOf(item)
        if (position >= 0) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Xóa tất cả items
     */
    fun clearItems() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    /**
     * Cập nhật item tại vị trí cụ thể
     */
    fun updateItemAt(position: Int, item: T) {
        if (position >= 0 && position < items.size) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    /**
     * Tạo DiffCallback cho 2 danh sách
     */
    abstract fun getDiffCallback(oldItems: List<T>, newItems: List<T>): DiffUtil.Callback
}

/**
 * Base DiffUtil.Callback
 */
abstract class BaseDiffCallback<T>(
    protected val oldItems: List<T>, 
    protected val newItems: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    /**
     * Kiểm tra xem 2 item có cùng id không (là cùng một object)
     */
    abstract override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean

    /**
     * Kiểm tra xem nội dung của 2 item có giống nhau không
     */
    abstract override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
} 