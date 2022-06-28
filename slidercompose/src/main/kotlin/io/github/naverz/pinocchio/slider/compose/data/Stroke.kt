/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose.data

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

sealed class Stroke(open val width: Dp) {
    data class WidthColor(override val width: Dp, val color: Color) : Stroke(width)
    data class WidthBrush(override val width: Dp, val brush: Brush) : Stroke(width)
}

internal fun Modifier.stroke(stroke: Stroke, shape: Shape): Modifier =
    when (stroke) {
        is Stroke.WidthBrush -> this.border(
            width = stroke.width,
            brush = stroke.brush,
            shape = shape
        )
        is Stroke.WidthColor -> this.border(
            width = stroke.width,
            color = stroke.color,
            shape = shape
        )
    }