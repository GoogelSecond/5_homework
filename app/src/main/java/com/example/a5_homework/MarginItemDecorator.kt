package com.example.a5_homework

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecorator(
    private val verticalMargin: Int,
    private val horizontalMargin: Int,
    private val spanCount: Int = 1,
    private val orientation: Int = GridLayoutManager.VERTICAL
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (orientation == GridLayoutManager.VERTICAL) {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    top = verticalMargin
                }
                if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                    left = horizontalMargin
                }
            } else {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    left = horizontalMargin
                }
                if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                    top = verticalMargin
                }
            }

            right = horizontalMargin
            bottom = verticalMargin
        }
    }
}