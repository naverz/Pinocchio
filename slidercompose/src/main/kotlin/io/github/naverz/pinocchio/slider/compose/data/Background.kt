/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose.data

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape


sealed class Background(open val shape: Shape) {
    @Immutable
    data class BrushShape(
        val brush: Brush,
        override val shape: Shape,
        @FloatRange(from = 0.0, to = 1.0)
        val alpha: Float = 1.0f
    ) : Background(shape = shape)

    @Immutable
    data class ColorShape(
        val color: Color,
        override val shape: Shape
    ) : Background(shape = shape)
}


internal fun Modifier.background(background: Background) =
    when (background) {
        is Background.BrushShape -> background(background.brush, background.shape, background.alpha)
        is Background.ColorShape -> background(background.color, background.shape)
    }
