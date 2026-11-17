package com.mmgpt.shoplistgpt

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mmgpt.shoplistgpt.adapter.ItemAdapter

internal data class ItemMove(val fromIndex: Int, val toIndex: Int)

internal object ItemReorderPositions {
    fun resolve(
        items: List<ShoppingItem>,
        fromPosition: Int,
        toPosition: Int
    ): ItemMove? {
        if (fromPosition !in items.indices || toPosition !in items.indices) return null
        if (fromPosition == toPosition) return null
        if (items[fromPosition].isPlaceholder || items[toPosition].isPlaceholder) return null

        return ItemMove(fromPosition - 1, toPosition - 1)
    }
}

class ItemDragReorderController(
    recyclerView: RecyclerView,
    private val adapter: ItemAdapter,
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
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition
            val move = ItemReorderPositions.resolve(
                adapter.currentList,
                fromPosition,
                toPosition
            ) ?: return false

            onReorder(move.fromIndex, move.toIndex)
            adapter.moveItem(fromPosition, toPosition)
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
