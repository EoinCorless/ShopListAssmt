package com.mmgpt.shoplistgpt

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

internal fun RecyclerView.setCategoryDoubleTapNavigation(
    viewPager: ViewPager2,
    categories: () -> List<ShoppingCategory>?,
    categoryIdAtPosition: (Int) -> Long?,
    categoryPageOffset: Int = 0
) {
    val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            val child = findChildViewUnder(e.x, e.y) ?: return false
            val position = getChildAdapterPosition(child)
            if (position == RecyclerView.NO_POSITION) return false

            val categoryId = categoryIdAtPosition(position) ?: return false
            val categoryIndex = categories()?.indexOfFirst { it.id == categoryId } ?: return false
            if (categoryIndex < 0) return false

            val targetPage = categoryIndex + categoryPageOffset
            if (targetPage !in 0 until (viewPager.adapter?.itemCount ?: 0)) return false

            viewPager.currentItem = targetPage
            return true
        }
    })

    addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(
            recyclerView: RecyclerView,
            event: MotionEvent
        ): Boolean {
            gestureDetector.onTouchEvent(event)
            return false
        }
    })
}
