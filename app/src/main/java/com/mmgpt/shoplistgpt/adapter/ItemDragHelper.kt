package com.mmgpt.shoplistgpt.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemDragHelper(
    recyclerView: RecyclerView,
    private val adapter: ItemAdapter,
    private val listOffset: Int,
    private val onReorder: (fromIndex: Int, toIndex: Int) -> Unit
) {
    private val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.bindingAdapterPosition
            val to = target.bindingAdapterPosition
            if (from == RecyclerView.NO_POSITION || to == RecyclerView.NO_POSITION) return false

            val itemFrom = adapter.currentList.getOrNull(from)
            val itemTo = adapter.currentList.getOrNull(to)
            if (itemFrom?.isPlaceholder == true || itemTo?.isPlaceholder == true) return false

            onReorder(from - listOffset, to - listOffset)
            adapter.moveItem(from, to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

        override fun isLongPressDragEnabled(): Boolean = false
    })

    init {
        touchHelper.attachToRecyclerView(recyclerView)
    }

    fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
}
