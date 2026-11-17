package com.mmgpt.shoplistgpt

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ItemReorderPositionsTest {
    private val items = listOf(
        ShoppingItem(-1, "", isPlaceholder = true),
        ShoppingItem(1, "First"),
        ShoppingItem(2, "Second"),
        ShoppingItem(-2, "", isPlaceholder = true)
    )

    @Test
    fun resolvesMovesInBothDirectionsToModelIndices() {
        assertEquals(ItemMove(0, 1), ItemReorderPositions.resolve(items, 1, 2))
        assertEquals(ItemMove(1, 0), ItemReorderPositions.resolve(items, 2, 1))
    }

    @Test
    fun rejectsPlaceholderAndInvalidPositions() {
        assertNull(ItemReorderPositions.resolve(items, 0, 1))
        assertNull(ItemReorderPositions.resolve(items, 1, 3))
        assertNull(ItemReorderPositions.resolve(items, -1, 1))
        assertNull(ItemReorderPositions.resolve(items, 1, items.size))
    }
}
