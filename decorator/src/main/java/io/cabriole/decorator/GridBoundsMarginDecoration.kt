/*
 * Copyright (c) 2020. Cabriole
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cabriole.decorator

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A [RecyclerView.ItemDecoration] that applies a margin to the bounds of the RecyclerView
 *
 * @param leftMargin margin to be applied to the left bound
 *
 * @param topMargin margin to be applied to the top bound
 *
 * @param orientation the orientation of the RecyclerView. Default is [RecyclerView.VERTICAL]
 *
 * @param inverted true if the LayoutManager is inverted and items are laid out
 * from the bottom to the top or from the right to the left.
 *
 * @param columnProvider a [ColumnProvider] that provides the number of columns
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class GridBoundsMarginDecoration(
    @Px private var leftMargin: Int = 0,
    @Px private var topMargin: Int = 0,
    @Px private var rightMargin: Int = 0,
    @Px private var bottomMargin: Int = 0,
    private var columnProvider: ColumnProvider,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [GridBoundsMarginDecoration] that applies the same margin to all sides
         */
        @JvmStatic
        fun create(
            @Px margin: Int,
            gridLayoutManager: GridLayoutManager,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): GridBoundsMarginDecoration {
            return GridBoundsMarginDecoration(
                leftMargin = margin,
                topMargin = margin,
                rightMargin = margin,
                bottomMargin = margin,
                columnProvider = object : ColumnProvider {
                    override fun getNumberOfColumns(): Int = gridLayoutManager.spanCount
                },
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }

        /**
         * Creates a [GridBoundsMarginDecoration] that applies the same margin to all sides
         */
        @JvmStatic
        fun create(
            @Px margin: Int,
            columnProvider: ColumnProvider,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): GridBoundsMarginDecoration {
            return GridBoundsMarginDecoration(
                leftMargin = margin,
                topMargin = margin,
                rightMargin = margin,
                bottomMargin = margin,
                columnProvider = columnProvider,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }
    }

    fun setMargin(margin: Int) {
        setMargin(left = margin, top = margin, right = margin, bottom = margin)
    }

    fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        this.leftMargin = left
        this.topMargin = top
        this.rightMargin = right
        this.bottomMargin = bottom
    }

    fun getLeftMargin() = leftMargin

    fun getTopMargin() = topMargin

    fun getRightMargin() = rightMargin

    fun getBottomMargin() = bottomMargin

    fun setColumnProvider(columnProvider: ColumnProvider) {
        this.columnProvider = columnProvider
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    fun getOrientation() = orientation

    fun setInverted(isInverted: Boolean) {
        this.inverted = isInverted
    }

    fun isInverted() = inverted

    fun setDecorationLookup(decorationLookup: DecorationLookup?) {
        this.decorationLookup = decorationLookup
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val columns = columnProvider.getNumberOfColumns()
        val columnIndex = position.rem(columns)
        val lines = Math.ceil(layoutManager.itemCount / columns.toDouble()).toInt()
        val lineIndex = position / columns

        if (orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(outRect, columns, columnIndex, lines, lineIndex)
        } else {
            applyHorizontalOffsets(outRect, columns, columnIndex, lines, lineIndex)
        }
    }

    private fun applyVerticalOffsets(
        outRect: Rect,
        columns: Int,
        columnIndex: Int,
        lines: Int,
        lineIndex: Int
    ) {
        if (columnIndex == 0) {
            outRect.left = leftMargin
            if (columns == 1) {
                outRect.right = rightMargin
            }
        } else if (columnIndex == columns - 1) {
            outRect.right = rightMargin
        }

        if (lineIndex == 0) {
            if (!inverted) {
                outRect.top = topMargin
            } else {
                outRect.bottom = bottomMargin
            }
        } else if (lineIndex == lines - 1) {
            if (!inverted) {
                outRect.bottom = bottomMargin
            } else {
                outRect.top = topMargin
            }
        }
    }

    private fun applyHorizontalOffsets(
        outRect: Rect,
        columns: Int,
        columnIndex: Int,
        lines: Int,
        lineIndex: Int
    ) {
        if (columnIndex == 0) {
            outRect.top = topMargin
            if (columns == 1) {
                outRect.bottom = bottomMargin
            }
        } else if (columnIndex == columns - 1) {
            outRect.bottom = bottomMargin
        }

        if (lineIndex == 0) {
            if (!inverted) {
                outRect.left = leftMargin
            } else {
                outRect.right = leftMargin
            }
        } else if (lineIndex == lines - 1) {
            if (!inverted) {
                outRect.right = topMargin
            } else {
                outRect.left = topMargin
            }
        }
    }

}
